package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.packets.KeepAlive;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketDecoder;
import org.salondesdevs.superdungeonsdestroyers.library.packets.PacketEncoder;
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
        logger.info("Connecting to server");

        String host = "localhost";
        int port = 9000;
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
            ChannelFuture f = b.connect(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        batch = new SpriteBatch();
        font = new BitmapFont();


    }


    SpriteBatch batch;

    BitmapFont font;


    boolean test = true;

    @Override
    public void begin() {
        batch.begin();
    }

    float remaining = 4;

    @Override
    public void process() {
        // Send enqueued packets, ...

        remaining -= world.getDelta();

//        if (remaining > 0) {
//            font.draw(batch, "Ingame in " + remaining, 100, 100);
//        }

        if (remaining < 0 && test) {
            test = false;
//            this.world.push(IngameState.class);
            this.send(new KeepAlive());
        }
    }

    public void send(Packet packet) {
        this.channelHandlerContext.writeAndFlush(packet);
    }

    @Override
    public void end() {
        batch.end();
    }

    @Override
    public void dispose() {

    }

    public class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx)
                throws Exception {
            channelHandlerContext = ctx;
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
