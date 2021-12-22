package top.binaryx.garen.server.listener;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.helper.NodePathHelper;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.service.ZookeeperService;


@Slf4j
public class ServerChangedListener extends AbstractListener implements PathChildrenCacheListener {

    private final JobConfigService jobConfigService = SpringContextHolder.getBean(JobConfigService.class);

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        log.info("******\tevent:{}", event);
        String path = event.getData().getPath();
        if (!NodePathHelper.isServerIpNode(path)) {
            return;
        }
        log.info("type:{},path:{}", event.getType(), path);


        //应用上线下线
        String ip = StrUtil.subAfter(path, "/", true);
        if (PathChildrenCacheEvent.Type.CHILD_REMOVED == event.getType()
                || PathChildrenCacheEvent.Type.CHILD_ADDED == event.getType()) {
            jobConfigService.emptyIp(ip);
            leaderHandler.migrateJobs();
            return;
        }
    }
}
