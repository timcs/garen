package top.binaryx.garen.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import top.binaryx.garen.server.common.GarenContext;
import top.binaryx.garen.server.component.LeaderHandler;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.helper.NodePathHelper;
import top.binaryx.garen.server.service.ZookeeperService;


@Slf4j
public class LeaderChangedLatchListener implements LeaderLatchListener {

    private final LeaderHandler leaderHandler = SpringContextHolder.getBean(LeaderHandler.class);
    private final ZookeeperService zookeeperService = SpringContextHolder.getBean(ZookeeperService.class);

    @Override
    public void isLeader() {
        log.info("become leader server.");
        //成为leader 监听ip节点,给任务分配调度器
        //监听 /server/ip节点
        PathChildrenCache childrenCache = zookeeperService.getServerIpPathCache();
        childrenCache.getListenable().addListener(new ServerChangedListener());

        //注册leader节点
        zookeeperService.persistAndOverwriteEphemeral(NodePathHelper.getServerLeaderNode(), GarenContext.getInstance().getServer());

        try {
            leaderHandler.migrateJobs();
        } catch (Exception e) {
            log.error("leader migrate group error.");
        }
    }

    @Override
    public void notLeader() {
        log.info("become follower server.");
        zookeeperService.getServerIpPathCache().getListenable().clear();
    }
}