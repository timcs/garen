package top.binaryx.garen.server.common;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * TCP链接信息
 *
 */
@Data
public class ConnectionContext {

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * channel ID
     */
    private String channelId;

    /**
     * socket channel
     */
    private Channel channel;

}
