package top.binaryx.garen.server.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.leader.LeaderLatch;

import java.util.List;


public interface ZookeeperService {

    /**
     * 初始化注册中心.
     */
    void init() throws Exception;

    /**
     * 关闭注册中心.
     */
    void close();

    /**
     * 获取注册数据.
     *
     * @param key 键
     * @return 值
     */
    String get(String key);

    /**
     * 获取注册数据, 如果为null, 返回defaultValue.
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    String getDefault(String key, String defaultValue);

    /**
     * 获取数据是否存在.
     *
     * @param key 键
     * @return 数据是否存在
     */
    boolean isExisted(String key);

    /**
     * 持久化注册数据.
     *
     * @param key   键
     * @param value 值
     */
    void persistOrUpdate(String key, String value);

    /**
     * 更新注册数据.
     *
     * @param key   键
     * @param value 值
     */
    void update(String key, String value);

    /**
     * 删除注册数据.
     *
     * @param key 键
     */
    void remove(String key);


    /**
     * 直接获取操作注册中心的原生客户端.
     * 如：Zookeeper或Redis等原生客户端.
     *
     * @return 注册中心的原生客户端
     */
    CuratorFramework getClient();

    /**
     * 获取连接状态
     *
     * @return 连接状态
     */
    boolean isConnected();


    /**
     * 获取子节点名称集合.
     *
     * @param key 键
     * @return 子节点名称集合
     */
    List<String> getChildrenKeys(String key);

    /**
     * 持久化临时注册数据.
     *
     * @param key   键
     * @param value 值
     * @return 处理结果
     */
    boolean persistEphemeral(String key, String value);

    /**
     * 持久化临时注册数据，
     *
     * @param key   键
     * @param value 值
     */
    boolean persistAndOverwriteEphemeral(String key, String value);

    /**
     * 持久化临时顺序注册数据.
     *
     * @param key 键
     */
    void persistEphemeralSequential(String key);

    /**
     * 持久化顺序节点
     *
     * @param key
     * @param value
     */
    void persistSequentialOrUpdate(String key, String value);

    long getSessionId();

    PathChildrenCache getServerIpPathCache();

    TreeCache getGroupsTreeCache();

    void registryIp() throws Exception;

    LeaderLatch getLeaderLatch();
}
