/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package top.binaryx.garen.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.common.exception.GarenException;
import top.binaryx.garen.common.util.AddressUtil;
import top.binaryx.garen.server.common.GarenContext;
import top.binaryx.garen.server.helper.NodePathHelper;
import top.binaryx.garen.server.listener.LeaderChangedLatchListener;
import top.binaryx.garen.server.service.PropertiesService;
import top.binaryx.garen.server.service.ZookeeperService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ZookeeperServiceImpl implements ZookeeperService {
    @Autowired
    private PropertiesService propertiesService;

    private CuratorFramework client;
    private PathChildrenCache serverIpChildrenCache;
    private TreeCache groupsTreeCache;
    private LeaderLatchListener leaderLatchListener;
    private LeaderLatch leaderLatch;


    @Override
    public void init() throws Exception {
        int maxSleepMs = propertiesService.getInt("zk.maxSleepTimeMilliseconds", 3000);
        int maxRetries = propertiesService.getInt("zk.maxRetries", 3);

        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(
                propertiesService.getInt("zk.baseSleepTimeMilliseconds", 1000),
                maxRetries,
                maxSleepMs);

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(GarenContext.getInstance().getZkServer())
                .retryPolicy(retryPolicy)
                .namespace(GarenContext.getInstance().getZkNameSpace())
                .sessionTimeoutMs(propertiesService.getInt("zk.sessionTimeoutMilliseconds", 20000))
                .connectionTimeoutMs(propertiesService.getInt("zk.connectionTimeoutMilliseconds", 10000));

        client = builder.build();
        client.start();

        client.blockUntilConnected(maxSleepMs * maxRetries, TimeUnit.MILLISECONDS);
        if (!client.getZookeeperClient().isConnected()) {
            throw new KeeperException.OperationTimeoutException();
        }
    }

    @Override
    public void close() {
        CloseableUtils.closeQuietly(client);
    }


    @Override
    public String get(String key) {
        try {
            return new String(client.getData().forPath(key), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public String getDefault(String key, String defaultValue) {
        String directValue = get(key);
        return directValue == null ? defaultValue : directValue;
    }

    @Override
    public boolean isExisted(String key) {
        try {
            return null != client.checkExists().forPath(key);
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void persistOrUpdate(String key, String value) {
        try {
            if (!isExisted(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes(StandardCharsets.UTF_8));
            } else {
                update(key, value);
            }
        } catch (Exception e) {
            //todo
        }
    }

    @Override
    public void update(String key, String value) {
        try {
            client.inTransaction().check().forPath(key).and().setData().forPath(key, value.getBytes(StandardCharsets.UTF_8)).and().commit();
        } catch (Exception ex) {
            //todo
        }
    }

    @Override
    public void remove(String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (Exception ex) {
            //todo
        }
    }

    @Override
    public CuratorFramework getClient() {
        return client;
    }

    @Override
    public boolean isConnected() {
        return getClient().getZookeeperClient().isConnected();
    }


    @Override
    public List<String> getChildrenKeys(String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            return result;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean persistEphemeral(String key, String value) {
        try {
            if (!isExisted(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(StandardCharsets.UTF_8));
                return true;
            }
        } catch (Exception ex) {
            //todo
        }
        return false;
    }

    @Override
    public boolean persistAndOverwriteEphemeral(String key, String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception ex) {
//todo
            return false;
        }
    }

    @Override
    public void persistEphemeralSequential(String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
        } catch (Exception ex) {
            //todo
        }
    }


    @Override
    public void persistSequentialOrUpdate(String key, String value) {
        try {
            if (!isExisted(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(key, value.getBytes(StandardCharsets.UTF_8));
            } else {
                update(key, value);
            }
        } catch (Exception ex) {
            //todo
        }
    }

    @Override
    public long getSessionId() {
        try {
            return client.getZookeeperClient().getZooKeeper().getSessionId();
        } catch (Exception e) {
            log.error("zookeeper getSessionId error.", e);
            return 0;
        }
    }

    @Override
    public synchronized PathChildrenCache getServerIpPathCache() {
        if (Objects.isNull(serverIpChildrenCache)) {
            serverIpChildrenCache = new PathChildrenCache(client, NodePathHelper.getServerIpNode(), Boolean.TRUE);
            try {
                serverIpChildrenCache.start();
            } catch (Exception e) {
                log.error("start serverIpChildrenCache error.", e);
            }
        }
        return serverIpChildrenCache;
    }

    @Override
    public synchronized TreeCache getGroupsTreeCache() {
        if (Objects.isNull(groupsTreeCache)) {
            groupsTreeCache = new TreeCache(client, NodePathHelper.getGroupsNode());
            try {
                groupsTreeCache.start();
            } catch (Exception e) {
                log.error("start groupsChildrenCache error.", e);
            }
        }
        return groupsTreeCache;
    }

    @Override
    public void registryIp() throws Exception {
        String localHost = AddressUtil.getLocalHost();
        boolean persistResult = persistAndOverwriteEphemeral(NodePathHelper.getServerIpNode(localHost), StringUtils.EMPTY);
        if (!persistResult) {
            throw new GarenException(MessageEnum.PERSIST_IP_FAIL);
        }
    }

    @Override
    public LeaderLatch getLeaderLatch() {
        if (Objects.isNull(leaderLatch)) {
            leaderLatchListener = new LeaderChangedLatchListener();
            leaderLatch = new LeaderLatch(getClient(), NodePathHelper.SERVER_LATCH_NODE);
            leaderLatch.addListener(leaderLatchListener);
        }
        return leaderLatch;
    }
}
