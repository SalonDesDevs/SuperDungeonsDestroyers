package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class ThatsYou extends Packet {
    public int me;

    public ThatsYou() {
    }

    public ThatsYou(int me) {
        this.me = me;
    }

    @Override
    public void read(ByteBuf in) {
        this.me = in.readInt();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.me);
    }
}
