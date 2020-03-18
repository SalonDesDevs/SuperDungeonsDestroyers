package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.eventbus.Subscribe;
import org.salondesdevs.superdungeonsdestroyers.library.events.net.PacketReceivedEvent;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketDecoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketEncoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerName;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.VersionCheckSuccess;
import org.salondesdevs.superdungeonsdestroyers.library.utils.ProtocolVersion;
import org.salondesdevs.superdungeonsdestroyers.states.IngameState;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.MainMenuScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.FutureListener;
import net.wytrem.ecs.BaseSystem;

@Singleton
public class NetworkSystem extends BaseSystem {

    private static final Logger logger = LoggerFactory.getLogger(NetworkSystem.class);

    @Inject
    NetworkHandlerSystem networkHandler;

    ChannelHandlerContext channelHandlerContext;

    EventLoopGroup workerGroup;

    public void tryConnect(String host, int port, MainMenuScreen screen) {
        logger.info("Connecting to server at {}:{}", host, port);

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
                        screen.connectFailed(f.cause());
                    });
                }
                else {
                    logger.info("Successfully connected to {}:{}", host, port);
                    Gdx.app.postRunnable(screen::connectSuccess);
                }
            });


            f.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        logger.info("onPacketReceived(" + "packetReceivedEvent = " + packetReceivedEvent + ")");

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
