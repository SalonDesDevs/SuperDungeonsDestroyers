package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

import io.netty.buffer.ByteBuf;

public abstract class EntityPacket extends Packet {

    public int entityId;

    protected EntityPacket() {

    }

    protected EntityPacket(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void read(ByteBuf in) {
        this.entityId = in.readInt();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.entityId);
    }
}
