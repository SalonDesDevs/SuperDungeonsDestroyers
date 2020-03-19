package org.salondesdevs.superdungeonsdestroyers.library.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    private static final Logger logger = LoggerFactory.getLogger(PacketEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
//        logger.trace("encode(" + "ctx = " + ctx + ", msg = " + msg + ", out = " + out + ")");

        try {
            out.writeByte(Packet.getId(msg));
            msg.write(out);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
