package org.salondesdevs.superdungeonsdestroyers.server.systems;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerPacketHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger( ServerPacketHandler.class );

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            Packet packet = (Packet) msg;
            logger.info("Received {}", packet.getClass().getSimpleName());
        }
}

