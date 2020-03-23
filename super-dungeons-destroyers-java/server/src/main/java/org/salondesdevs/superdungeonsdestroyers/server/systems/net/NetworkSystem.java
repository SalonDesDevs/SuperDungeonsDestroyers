package org.salondesdevs.superdungeonsdestroyers.server.systems.net;

import com.google.inject.Injector;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.wytrem.ecs.BaseSystem;
import net.wytrem.ecs.Component;
import net.wytrem.ecs.Mapper;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketDecoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class NetworkSystem extends BaseSystem {
    private static final Logger logger = LoggerFactory.getLogger(NetworkSystem.class);

    @Inject
    Injector injector;

    List<NetHandler> netHandlerList = new ArrayList<>();

    EventLoopGroup bossGroup, workerGroup;

    @Inject
    Mapper<PlayerConnection> playerConnectionMapper;

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

    public void send(int player, Iterable<Packet> packetIterable) {
        this.playerConnectionMapper.get(player).send(packetIterable);
    }

    public void send(int player, Packet... packets) {
        this.playerConnectionMapper.get(player).send(packets);
    }

    public void broadcast(Packet... packets) {
        this.playerConnectionMapper.forEachValue(playerConnection -> playerConnection.send(packets));
    }

    public void broadcast(Iterable<Packet> packets) {
        this.playerConnectionMapper.forEachValue(playerConnection -> playerConnection.send(packets));
    }

    public void broadcastExcluding(int playerId, Packet... packets) {
        this.playerConnectionMapper.forEach((i, playerConnection) -> {
            if (playerId != i) {
                playerConnection.send(packets);
            }
        });
    }

    public void registerPlayerConnection(int playerId, ChannelHandlerContext channelHandlerContext) {
        this.playerConnectionMapper.set(playerId, new PlayerConnection(channelHandlerContext));

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
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            logger.trace("Received {} from {}", msg.getClass().getSimpleName(), ctx);
            this.netHandler.enqeueForHandling((Packet) msg);
        }
    }

    @Override
    public void dispose() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    private static class PlayerConnection implements Component {
        private ChannelHandlerContext channelHandlerContext;

        public PlayerConnection(ChannelHandlerContext channelHandlerContext) {
            this.channelHandlerContext = channelHandlerContext;
        }

        public void send(Iterable<Packet> packets) {
            for (Packet packet : packets) {
                this.channelHandlerContext.write(packet);
            }
            this.channelHandlerContext.flush();
        }

        public void send(Packet...packets) {
            for (Packet packet : packets) {
                this.channelHandlerContext.write(packet);
            }
            this.channelHandlerContext.flush();
        }
    }
}
