package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import com.badlogic.gdx.Gdx;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.FutureListener;
import net.wytrem.ecs.BaseSystem;
import org.salondesdevs.superdungeonsdestroyers.events.ConnectFailedEvent;
import org.salondesdevs.superdungeonsdestroyers.events.ConnectSucceedEvent;
import org.salondesdevs.superdungeonsdestroyers.library.events.core.EventBus;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketDecoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketEncoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.VersionCheckSuccess;
import org.salondesdevs.superdungeonsdestroyers.library.utils.ProtocolVersion;
import org.salondesdevs.superdungeonsdestroyers.states.IngameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NetworkSystem extends BaseSystem {

    private static final Logger logger = LoggerFactory.getLogger(NetworkSystem.class);

    @Inject
    NetworkHandlerSystem networkHandler;

    ChannelHandlerContext channelHandlerContext;

    EventLoopGroup workerGroup;

    @Inject
    EventBus eventBus;

    public void tryConnectToServer(String host, int port) {
        logger.info("Try connecting to server at {}:{}", host, port);

        workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch)
                    throws Exception {
                ch.pipeline().addLast(new PacketEncoder(),
                        new PacketDecoder(), new ClientHandler());
            }
        });

        try {
            ChannelFuture f = b.connect(host, port);

            f.addListener((FutureListener<Void>) future -> {
                if (!f.isSuccess()) {
                    logger.error("Could not connect to server at {}:{}", host, port);

                    if (f.cause() != null) {
                        logger.error("Throwable: ", f.cause());
                    }
                    else {
                        logger.error("ChannelFuture#cause() (throwable) is null.");
                    }

                    Gdx.app.postRunnable(() -> {
                        eventBus.post(new ConnectFailedEvent(f.cause()));
                    });
                }
                else {
                    logger.info("Successfully connected to {}:{}", host, port);
                    Gdx.app.postRunnable(() -> {
                        eventBus.post(new ConnectSucceedEvent());
                    });
                }
            });


            f.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof VersionCheckSuccess) {
            world.push(IngameState.class);
        }
    }

    public void send(Packet packet) {
        this.channelHandlerContext.writeAndFlush(packet);
    }

    @Override
    public void dispose() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    public class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx)
                throws Exception {
            channelHandlerContext = ctx;
            logger.debug("Sending VersionCheck({}, {})", ProtocolVersion.MAJOR, ProtocolVersion.MINOR);
            send(new VersionCheck(ProtocolVersion.MAJOR, ProtocolVersion.MINOR));
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            networkHandler.enqueue((Packet) msg);
        }
    }
}
