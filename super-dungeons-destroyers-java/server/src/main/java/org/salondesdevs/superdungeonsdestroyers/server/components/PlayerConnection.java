package org.salondesdevs.superdungeonsdestroyers.server.components;

import io.netty.channel.ChannelHandlerContext;
import net.wytrem.ecs.Component;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class PlayerConnection implements Component {
    private ChannelHandlerContext channelHandlerContext;

    public PlayerConnection(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public void sendAll(Iterable<Packet> packets) {
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
