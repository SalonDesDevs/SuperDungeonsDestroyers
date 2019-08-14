package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class Welcome extends Packet {
    public int me;

    public Welcome() {
    }

    public Welcome(int me) {
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
