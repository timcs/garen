package top.binaryx.garen.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.service.ZookeeperService;


@Slf4j
public class ConnectionStateChangedListener implements ConnectionStateListener {

    protected final ZookeeperService zookeeperService = SpringContextHolder.getBean(ZookeeperService.class);

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        log.warn("newState.{}.isLeader:{}", newState, zookeeperService.getLeaderLatch().hasLeadership());
        try {
//          不关心  LOST,SUSPENDED,READ_ONLY,CONNECTED
            if (ConnectionState.RECONNECTED == newState) {
                log.info("RECONNECTED");
                //注册ip
                zookeeperService.registryIp();
            }
        } catch (Exception e) {
            log.error("error.", e);
        }
    }
}
