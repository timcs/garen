package top.binaryx.garen.server.component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.binaryx.garen.common.exception.SocketServerStopException;
import top.binaryx.garen.server.service.PropertiesService;

/**
 * <>
 *
 * @author tim
 * @date 2022-02-11 16:51
 * @since
 */
@Slf4j
@Component
public class GarenNettyServer {

    private final GarenChannelInitializer initializer;

    private Channel channel;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    PropertiesService propertiesService;

    @Autowired
    public GarenNettyServer(GarenChannelInitializer initializer, PropertiesService propertiesService) {
        this.initializer = initializer;
        this.propertiesService = propertiesService;
    }

    public void start() {
        int threads = SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() + 1) * 2;
        int workerGroupThreads = propertiesService.getInt("server.num.network.threads", threads);
        int port = propertiesService.getInt("server.tcp.port", 6000);
        int soBacklog = propertiesService.getInt("server.tcp.soBacklog", 1024);
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(workerGroupThreads);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, soBacklog)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(initializer);

        try {
            channel = serverBootstrap.bind(port).sync().channel();
        } catch (Exception e) {
            log.error("start netty server error.", e);
        }
        log.info("netty server socket started, listening port:{},workerGroupThreads:{}", port, workerGroupThreads);
    }

    public void close() {
        if (null == channel) {
            throw new SocketServerStopException();
        }

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        bossGroup = null;
        workerGroup = null;
        channel = null;
        log.info("garen netty server stopped.");
    }

}
