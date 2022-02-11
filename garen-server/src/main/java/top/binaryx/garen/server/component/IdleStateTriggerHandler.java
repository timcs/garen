package top.binaryx.garen.server.component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import top.binaryx.garen.common.Constant;
import top.binaryx.garen.server.common.ContextManager;

/**
 * 连接空闲状态读超时处理.
 *
 */
@Slf4j
@Sharable
public class IdleStateTriggerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        if (event instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) event).state();
            if (state == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                String channelId = channel.attr(AttributeKey.valueOf(Constant.SESSION_ID)).get().toString();
                ContextManager.removeConnection(channelId);
                log.warn("Close connection when heartbeat expired. clientId:{}, sessionId:{}", channel.remoteAddress().toString(), channelId);
                channel.close();
            }
        } else {
            super.userEventTriggered(ctx, event);
        }
    }

}
