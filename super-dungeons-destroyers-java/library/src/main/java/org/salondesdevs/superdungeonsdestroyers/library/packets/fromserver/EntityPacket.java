package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public abstract class EntityPacket extends Packet {

    private int entity;

    protected EntityPacket() {

    }

    protected EntityPacket(int entity) {
        this.entity = entity;
    }

    @Override
    public void read(ByteBuf in) {
        this.entity = in.readInt();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.entity);
    }

    public int getEntity() {
        return entity;
    }
}
