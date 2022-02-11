package top.binaryx.garen.server.component;

import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.binaryx.garen.common.MessagePackProtobuf;

import java.net.InetSocketAddress;

/**
 * 服务端消息处理.
 *
 */
@Slf4j
@Sharable
@Component
public class GarenServerHandler extends SimpleChannelInboundHandler<MessagePackProtobuf.Message> {

    private static final MessagePackProtobuf.Message HEARTBEAT_PONG = MessagePackProtobuf.Message.newBuilder()
            .setMessageType(MessageTypeEnum.HEARTBEAT_RESP.getCode())
            .build();

    /**
     * 消息验证处理
     */
    private ServerEventVerifier                      eventVerifier;

    /**
     * 消息分发处理
     */
    private final ServerEventDispatcher              eventDispatcher;

    @Autowired
    public GarenServerHandler(ServerEventVerifier eventVerifier, ServerEventDispatcher eventDispatcher) {
        this.eventVerifier = eventVerifier;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 链路激活，给链路设置ID
        Channel channel = ctx.channel();
        String channelId = ChannelIdGenerator.newInstance().asShortText();
        channel.attr(AttributeKey.valueOf(Constants.SESSION_ID)).set(channelId);
        log.info("Channel active, remote ip address [{}], sessionId [{}]", channel.remoteAddress().toString(),
                channelId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String channelId = channel.attr(AttributeKey.valueOf(Constants.SESSION_ID)).get().toString();
        ContextManager.removeConnection(channelId);
        log.warn("Channel inactive, remote ip address [{}], sessionId [{}]", channel.remoteAddress().toString(),
                channelId);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("Channel registered, remote ip address [{}]", channel.remoteAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        log.error("Channel close exception, remote ip address [{}].", channel.remoteAddress().toString(), cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePackProtobuf.Message msg) throws Exception {
        if (msg.getMessageType().equals(MessageTypeEnum.HEARTBEAT_REQ.getCode())) {
            log.debug("HEARTBEAT_PONG");
            ctx.writeAndFlush(HEARTBEAT_PONG);
        } else {
            // 先走验证处理
            CommonResponse response = eventVerifier.handleMessage(msg);
            if (null == response) {
                // 获取IP信息
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                MessageContext messageContext = new MessageContext();
                messageContext.setVersion(msg.getVersion());
                messageContext.setRequestId(msg.getRequestId());
                messageContext.setType(msg.getMessageType());
                messageContext.setBody(msg.getMessage().toByteArray());
                messageContext.setClientId(address.getAddress().getHostAddress());
                // 协议类型 TCP默认bean模式
                messageContext.setProtocolType(ProtocolTypeEnum.BEAN.getCode());
                messageContext.setHostname(address.getHostName());
                messageContext
                        .setSessionId(ctx.channel().attr(AttributeKey.valueOf(Constants.SESSION_ID)).get().toString());
                if (msg.getMessageType().equals(MessageTypeEnum.BROKER_METADATA_REQ.getCode())) {
                    // BROKER_METADATA_REQ则缓存客户端信息
                    cacheClientConnection(messageContext, ctx);
                }
                // 分发消息到各自的处理器中处理
                eventDispatcher.dispatch(messageContext);
            } else {
                // 验证未通过
                MessagePackProtobuf.Message.Builder builder = MessagePackProtobuf.Message.newBuilder();
                builder.setMessageType(MessageTypeEnum.COMMON_RESP.getCode());
                builder.setMessage(ByteString.copyFrom(ProtobufUtil.serializer(response)));
                builder.setRequestId(msg.getRequestId());
                ctx.writeAndFlush(builder.build());
                ctx.close();
            }
        }
    }

    private void cacheClientConnection(MessageContext messageContext, ChannelHandlerContext ctx) {
        // 缓存客户端链接信息
        ConnectionContext context = new ConnectionContext();
        context.setChannel(ctx.channel());
        context.setChannelId(messageContext.getSessionId());
        context.setClientIp(messageContext.getClientId());
        ContextManager.addConnection(context);
        log.info("Save connection info. {}", context);
    }
}
