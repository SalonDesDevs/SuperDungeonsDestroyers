package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntityDespawn extends Packet {

    public int entityId;

    public EntityDespawn() {

    }

    public EntityDespawn(int entityId) {
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
