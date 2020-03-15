package org.salondesdevs.superdungeonsdestroyers.library.packets;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 1) {
            return;
        }

        Packet packet = Packet.createFromId(in.readByte());
        packet.read(in);
        out.add(packet);
    }
}
