package top.binaryx.garen.server.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import top.binaryx.garen.common.enums.CommandEnum;
import top.binaryx.garen.common.enums.ProtocolTypeEnum;
import top.binaryx.garen.server.pojo.dto.Command;
import top.binaryx.garen.server.service.Callback;

/**
 * 任务客户端信息.
 *
 */
@Data
public class JobClientContext {

    /**
     * 应用名称(群组名)
     */
    private String sarName;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端hostname
     */
    private String hostname;

    /**
     * 客户端会话ID
     */
    private volatile String sessionId;

    /**
     * 客户端连接是否已经有效，默认是
     */
    private volatile boolean available = true;

    /**
     * 协议类型
     */
    private ProtocolTypeEnum protocolType;

    /**
     * 链接信息修改回调
     */
    @Setter(AccessLevel.PRIVATE)
    private Callback<Command<String>, Boolean> callback;

    private class JobClientConnectionCallback implements Callback<Command<String>, Boolean> {

        private final JobClientContext clientContext;

        JobClientConnectionCallback(JobClientContext clientContext) {
            this.clientContext = clientContext;
        }

        @Override
        public Boolean call(Command<String> command) {
            if (!command.getValue().equals(clientContext.getSessionId())) {
                return false;
            }
            if (command.getCommand().equals(CommandEnum.REMOVE)) {
                sessionId = null;
                available = false;
            }
            if (command.getCommand().equals(CommandEnum.UPDATE)) {
                sessionId = command.getValue();
                available = true;
            }
            return false;
        }
    }

    public JobClientContext() {
        this.callback = new JobClientConnectionCallback(this);
    }
}
