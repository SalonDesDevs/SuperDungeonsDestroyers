package org.salondesdevs.superdungeonsdestroyers.server.systems;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketDecoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class NetworkSystem extends BaseSystem {
    private static final Logger logger = LoggerFactory.getLogger(NetworkSystem.class);

    @Inject
    org.salondesdevs.superdungeonsdestroyers.server.Server sddServer;

    EventLoopGroup bossGroup, workerGroup;

    @Override
    public void initialize() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast(new PacketDecoder(),
                                new PacketEncoder(),
                                new ServerPacketHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            b.bind(9000).sync();
            logger.info("Server successfully opened");
        } catch (InterruptedException e) {
            logger.error("Failed to bind netty channel", e);
        }
    }

    @Override
    public void dispose() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
