package org.salondesdevs.superdungeonsdestroyers.server.systems;

import com.google.inject.Injector;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketDecoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Filter;

@Singleton
public class NetworkSystem extends BaseSystem {
    private static final Logger logger = LoggerFactory.getLogger(NetworkSystem.class);

    @Inject
    org.salondesdevs.superdungeonsdestroyers.server.Server sddServer;

    @Inject
    Injector injector;

    List<NetHandler> netHandlerList = new ArrayList<>();

    EventLoopGroup bossGroup, workerGroup;

    @Override
    public void initialize() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast(new PacketDecoder(),
                                new PacketEncoder(),
                                new ServerPacketAdapter());
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            serverBootstrap.bind(9000).sync();
            logger.info("Server successfully opened");
        } catch (InterruptedException e) {
            logger.error("Failed to bind netty server", e);
        }
    }

    public void broadcast(Packet...packets) {

            System.out.println("list = " + netHandlerList);

            netHandlerList.stream().map(NetHandler::getPlayerConnection).forEach(playerConnection -> playerConnection.send(packets));
    }

    public void broadcastExcluding(int playerId, Packet...packets) {
        this.broadcastFilter(netHandler -> netHandler.getPlayerId() != playerId, packets);
    }

    public void broadcastFilter(Predicate<NetHandler> filter, Packet...packets) {
        netHandlerList.stream().filter(filter).map(NetHandler::getPlayerConnection).forEach(playerConnection -> playerConnection.send(packets));
    }

    @Override
    public void process() {
        synchronized (this.netHandlerList) {
            this.netHandlerList.forEach(NetHandler::process);
        }
    }

    public class ServerPacketAdapter extends ChannelInboundHandlerAdapter {
        NetHandler netHandler;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            netHandler = injector.getInstance(NetHandler.class);
            netHandler.initialize(ctx);
            synchronized (netHandlerList) {
                netHandlerList.add(netHandler);
                logger.info("Actived new connection from {}", ctx.channel().remoteAddress());
                logger.info("list is now {}", netHandlerList);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            logger.info("Received {}", msg.getClass().getSimpleName());
            this.netHandler.enqeue((Packet) msg);
        }
    }

    @Override
    public void dispose() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
