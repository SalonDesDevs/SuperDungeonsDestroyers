package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.salondesdevs.superdungeonsdestroyers.library.packets.ByteBufHelper;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class DisconnectReason extends Packet {
    public String reason;

    public DisconnectReason() {
    }

    public DisconnectReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void read(ByteBuf in) {
        this.reason = ByteBufHelper.readString(in);
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufHelper.writeString(this.reason, out);
    }
}
