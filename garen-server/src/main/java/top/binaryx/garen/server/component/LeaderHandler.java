package top.binaryx.garen.server.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.binaryx.garen.common.Constant;
import top.binaryx.garen.server.common.GarenContext;
import top.binaryx.garen.server.helper.NodePathHelper;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.MigrateRequest;
import top.binaryx.garen.server.pojo.dto.MigrateResponse;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.service.ZookeeperService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Component
public class LeaderHandler {

    private static List<Long> toMigrateJobs = Lists.newArrayList();
    private static final Map<String, List<Long>> jobLoadCache = new LinkedHashMap<>();

    @Autowired
    ZookeeperService zookeeperService;

    @Autowired
    JobConfigService jobConfigService;

    /**
     * 任务迁移：
     * 1、获取所有的服务器
     * 2、获取所有的任务
     * 3、获取当前任务分配情况
     * 4、计算每台服务器平均应该接管的任务
     * 5、通知超载服务器释放任务
     */
    public synchronized void migrateJobs() throws Exception {
        log.info("start migrateJob");
        List<String> servers = zookeeperService.getChildrenKeys(NodePathHelper.getServerIpNode());
        Map<String, List<Long>> maps = getJobLoad();

        int total = maps.values().stream().mapToInt(List::size).reduce(0, Integer::sum);
        int avg = (int) Math.ceil((double) total / servers.size());

        handleDieAway(maps, servers);

        //移除未被接管的任务
        List<Long> noServers = maps.remove(CharSequenceUtil.EMPTY);
        toMigrateJobs.addAll(CollectionUtil.isEmpty(noServers) ? Lists.newArrayList() : noServers);

        //maps按照从大到小排序，把新ip加到最后
        Set<String> collect = Sets.newHashSet(maps.keySet());
        collect.addAll(servers);

        collect.forEach(ip -> {
            List<Long> load = maps.getOrDefault(ip, Lists.newArrayList());
            int size = load.size();
            if (size == avg) {
                return;
            }
            String url = String.format(Constant.MIGRATE_URL, ip, GarenContext.getInstance().getServerPort());
            MigrateRequest request = new MigrateRequest();
            request.setCount(avg - size);//负数代表释放,正数代表接管
            request.setJobIds(request.getCount() > 0 ? toMigrateJobs : null);
            //没有足够的待迁移任务
            if (request.getCount() > 0 && toMigrateJobs.isEmpty()) {
                return;
            }

            HttpResponse httpResponse = HttpUtil.createPost(url).body(new Gson().toJson(request)).execute();
            MigrateResponse response = new Gson().fromJson(httpResponse.body(), MigrateResponse.class);
            //释放之后要加到列表中,释放后要从列表中移除
            boolean flag = request.getCount() > 0 ? toMigrateJobs.removeAll(response.getJobIds()) : toMigrateJobs.addAll(response.getJobIds());
        });

        //清除缓存
        clearCache();
        log.info("finish migrateJob");
    }

    public static void clearCache() {
        jobLoadCache.clear();
        toMigrateJobs.clear();
    }

    /**
     * 移除假死的ip
     * 查看map中key的数量是否比servers多,多的那个有可能为迁移假死未正常卸载
     * 1、更新数据库
     * 2、尝试请求关闭调度器
     */
    public void handleDieAway(Map<String, List<Long>> maps, List<String> servers) {
        Set<String> keySet = Sets.newHashSet(maps.keySet());
        keySet.removeAll(servers);

        keySet.forEach(ip -> {
            if (StrUtil.isEmpty(ip)) {
                return;
            }
            jobConfigService.emptyIp(ip);
            maps.merge(CharSequenceUtil.EMPTY, Lists.newArrayList(), (pre, one) -> {
                pre.addAll(maps.remove(ip));
                return pre;
            });
            //为避免重复调度,尝试请求释放,不一定请求得通
            String url = String.format(Constant.SHUTDOWN_SCHEDULER_URL, ip, GarenContext.getInstance().getServerPort());
            try {
                HttpResponse response = HttpUtil.createPost(url).execute();
            } catch (Exception e) {
                log.error("execute:{} error.", url, e);
            }
        });
    }

    /**
     * 统计每台服务器接管的任务数,倒排序
     *
     * @return
     */
    public Map<String, List<Long>> getJobLoad() {
        if (jobLoadCache.isEmpty()) {
            buildCache();
        }
        return jobLoadCache;
    }

    public String getLowerIp() {
        Map<String, List<Long>> jobLoad = getJobLoad();
        String ip = (String) jobLoad.keySet().toArray()[jobLoad.size() - 1];
        return ip;
    }

    /**
     * 创建机器负载缓存,倒排序
     *
     * @return
     */
    public Map<String, List<Long>> buildCache() {
        List<JobConfigDTO> jobs = jobConfigService.findAll();
        Map<String, List<Long>> map = Maps.newHashMap();
        jobs.forEach(jobConfig -> {
            String key = StrUtil.nullToDefault(jobConfig.getExecutorIp(), CharSequenceUtil.EMPTY);
            map.merge(key, Lists.newArrayList(jobConfig.getId()), (pre, current) -> {
                pre.add(jobConfig.getId());
                return pre;
            });
        });

        Map<String, List<Long>> result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> {
                    if (o1.size() > o2.size()) {
                        return -1;
                    } else if (o1.size() < o2.size()) {
                        return 1;
                    } else {
                        return 0;
                    }
                })).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        result.putIfAbsent(GarenContext.getInstance().getServer(), Lists.newArrayList());
        jobLoadCache.clear();
        jobLoadCache.putAll(result);
        return jobLoadCache;
    }
}