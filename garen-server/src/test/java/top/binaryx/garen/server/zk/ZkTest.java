package top.binaryx.garen.server.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


public class ZkTest {
    public static void main(String[] args) throws Exception {
        CuratorFramework client;
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3, 3000);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString("10.232.7.3:2181")
                .retryPolicy(retry)
                .namespace("default")
                .sessionTimeoutMs(20000)
                .connectionTimeoutMs(10000);
        client = builder.build();
        client.start();
        client.blockUntilConnected(9000, TimeUnit.MILLISECONDS);
        if (!client.getZookeeperClient().isConnected()) {
            throw new RuntimeException();
        }

        final String path = "/path";
        final String value = "value";
//        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, value.getBytes(StandardCharsets.UTF_8));

//        client.getConnectionStateListenable().addListener((client1, newState) -> {
//            System.out.println(newState + ":" + getValue(client, path));
//        });

        while (true) {
            System.err.println(getValue(client, path));
            Thread.sleep(1000);
        }
    }

    private static String getValue(CuratorFramework client, String path) {
        try {
            return new String(client.getData().forPath(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
