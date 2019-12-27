package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

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
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketDecoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketEncoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.utils.ProtocolVersion;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.MainMenuScreen;
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

    @Override
    public void initialize() {

    }

    public void tryConnect(String host, int port, MainMenuScreen screen) {
        logger.info("Connecting to server at {}:{}", host, port);

        EventLoopGroup workerGroup = new NioEventLoopGroup();

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
                        Gdx.app.postRunnable(() -> {
                            screen.setErrorText("Could not connect to server (" + f.cause().getLocalizedMessage() + ").");
                        });
                    }
                    else {
                        screen.setErrorText("Could not connect to server (null throwable).");
                        Gdx.app.postRunnable(() -> {
                           logger.error("ChannelFuture#cause() (throwable) is null.");
                        });
                    }
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

    public void send(Packet packet) {
        this.channelHandlerContext.writeAndFlush(packet);
    }

    @Override
    public void dispose() {

    }

    public class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx)
                throws Exception {
            channelHandlerContext = ctx;
            logger.info("Sending VersionCheck({}, {})", ProtocolVersion.MAJOR, ProtocolVersion.MINOR);
            send(new VersionCheck(ProtocolVersion.MAJOR, ProtocolVersion.MINOR));
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            synchronized (networkHandler.messagesToHandle) {
                networkHandler.messagesToHandle.add((Packet) msg);
            }
        }
    }
}
