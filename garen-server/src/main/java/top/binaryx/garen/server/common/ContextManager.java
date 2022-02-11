package top.binaryx.garen.server.common;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-02-11 17:30
 * @since
 */
public class ContextManager {

    private final static Map<String, ConnectionContext> CONNECTIONS = new ConcurrentHashMap<>();

    /**
     * 获取指定链接信息
     *
     * @param sessionId 链接Id
     * @return 链接信息
     */
    public static ConnectionContext getConnection(String sessionId) {
        if (StrUtil.isBlank(sessionId)) {
            return null;
        }
        return CONNECTIONS.get(sessionId);
    }

    /**
     * 移除指定链接信息
     *
     * @param sessionId 链接Id
     */
    public static void removeConnection(String sessionId) {
        ConnectionContext connectionContext = getConnection(sessionId);
        if (null != connectionContext  && connectionContext.getClientIp() != null) {
            JobClientContext context = getJobClient(connectionContext.getSarName(), connectionContext.getClientIp());
            if (null != context) {
                // 更新客户端上下文状态
                context.getCallback().call(new Command<>(CommandEnum.REMOVE, sessionId));
            }

            JobGroupContext groupContext = GROUPS.get(connectionContext.getSarName());
            if (null != groupContext) {
                // 客户端移出，重新设置分片标识
                GROUPS.get(connectionContext.getSarName()).needSharding();
            }
        }
        CONNECTIONS.remove(sessionId);
    }
}
