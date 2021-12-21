package top.binaryx.garen.server.component;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

    private List<Long> toMigrateJobs = Lists.newArrayList();
    private static final Cache<String, List<Long>> jobLoadCache = CacheBuilder.newBuilder().maximumSize(5).build();

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
    public void migrateJobs() throws Exception {
        List<String> servers = zookeeperService.getChildrenKeys(NodePathHelper.getServerIpNode());
        Map<String, List<Long>> maps = getJobLoad();
        if (maps.isEmpty()) {
            return;
        }

        int total = maps.values().stream().mapToInt(List::size).reduce(0, Integer::sum);
        int avg = Math.floorDiv(total, servers.size());

        //移除未被接管的任务
        toMigrateJobs.addAll(maps.remove(CharSequenceUtil.EMPTY));

        //maps按照从大到小排序，把新ip加到最后
        Set<String> collect = maps.keySet();
        collect.addAll(servers);

        servers.forEach(ip -> {
            List<Long> load = maps.getOrDefault(ip, Lists.newArrayList());
            int size = load.size();
            if (size == avg) {
                return;
            }
            String url = String.format(Constant.MIGRATE_URL, ip, GarenContext.getInstance().getServerPort());
            MigrateRequest request = new MigrateRequest();
            request.setCount(avg - size);//负数代表释放,整数代表接管
            request.setJobIds(request.getCount() > 0 ? toMigrateJobs : null);

            HttpResponse httpResponse = HttpUtil.createPost(url).body(new Gson().toJson(request)).execute();
            MigrateResponse response = new Gson().fromJson(httpResponse.body(), MigrateResponse.class);
            //释放之后要加到列表中,释放后要从列表中移除
            boolean flag = request.getCount() > 0 ? toMigrateJobs.removeAll(response.getJobIds()) : toMigrateJobs.addAll(response.getJobIds());
        });

        //清除缓存
        jobLoadCache.invalidateAll();
        toMigrateJobs.clear();
    }

    /**
     * 统计每台服务器接管的任务数,倒排序
     *
     * @return
     */
    private Map<String, List<Long>> getJobLoad() {
        if (jobLoadCache.size() == 0) {
            buildCache();
        }
        return jobLoadCache.asMap();
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
    private Cache<String, List<Long>> buildCache() {
        List<JobConfigDTO> jobs = jobConfigService.findAll();
        Map<String, List<Long>> map = Maps.newHashMap();
        jobs.forEach(jobConfig -> map.compute(StrUtil.nullToDefault(jobConfig.getExecutorIp(), CharSequenceUtil.EMPTY),
                (k, v) -> {
                    if (map.containsKey(k)) {
                        v.add(jobConfig.getId());
                        return v;
                    } else {
                        return Lists.newArrayList(jobConfig.getId());
                    }
                }));

        Map<String, List<Long>> result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> {
                    if (o1.size() > o2.size()) {
                        return 1;
                    } else if (o1.size() < o2.size()) {
                        return -1;
                    } else {
                        return 0;
                    }
                })).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        jobLoadCache.putAll(result);
        return jobLoadCache;
    }
}