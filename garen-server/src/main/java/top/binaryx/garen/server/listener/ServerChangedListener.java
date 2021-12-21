package top.binaryx.garen.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import top.binaryx.garen.server.helper.NodePathHelper;


@Slf4j
public class ServerChangedListener extends AbstractListener implements PathChildrenCacheListener {

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        String path = event.getData().getPath();
        if (!NodePathHelper.isServerIpNode(path)) {
            return;
        }

        //应用上线下线
        if (PathChildrenCacheEvent.Type.CHILD_ADDED == event.getType()
                || PathChildrenCacheEvent.Type.CHILD_REMOVED == event.getType()) {
            leaderHandler.migrateJobs();
            return;
        }
    }
}
