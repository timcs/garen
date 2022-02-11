package top.binaryx.garen.server.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import top.binaryx.garen.common.util.AddressUtil;
import top.binaryx.garen.server.common.GarenContext;
import top.binaryx.garen.server.listener.ConnectionStateChangedListener;
import top.binaryx.garen.server.service.ZookeeperService;
import top.binaryx.garen.server.util.ThreadUtil;


@Slf4j
@Component
public class ServerBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ZookeeperService zookeeperService;

    @Autowired
    private GarenNettyServer nettyServer;

    @Value("${zk.server}")
    String zkServer;

    @Value("${server.port}")
    int serverPort;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (true) {
            return;
        }
        try {
            initParams();

            //初始化zk
            zookeeperService.init();

            zookeeperService.getClient().getConnectionStateListenable().addListener(new ConnectionStateChangedListener());

            //加入Leader选举
            joinLeaderElect();

            //注册本机ip
            zookeeperService.registryIp();

            //启动netty
            nettyServer.start();
        } catch (Exception e) {
            log.error("init server error.", e);
        }
    }


    private void joinLeaderElect() {
        ThreadUtil.newSingleThreadExecutor().submit(() -> {
            LeaderLatch leaderLatch = zookeeperService.getLeaderLatch();
            log.info("leaderLatch is leader:{}", leaderLatch.hasLeadership());
            try {
                leaderLatch.start();
                leaderLatch.await();
            } catch (Exception e) {
                log.error("Failed to elect a Leader! will retry", e);
            }
        });
    }

    /**
     * 解析参数
     * 解析zk参数
     */
    private void initParams() throws Exception {
        int start = zkServer.lastIndexOf("/");
        if (start >= 0) {
            String namespace = zkServer.substring(start + 1);
            String servers = zkServer.substring(0, start);
            GarenContext.getInstance().setZkNameSpace(namespace);
            GarenContext.getInstance().setZkServer(servers);
        } else {
            GarenContext.getInstance().setZkServer(zkServer);
        }
        GarenContext.getInstance().setServerPort(serverPort);
        GarenContext.getInstance().setServer(AddressUtil.getLocalHost());
    }
}
