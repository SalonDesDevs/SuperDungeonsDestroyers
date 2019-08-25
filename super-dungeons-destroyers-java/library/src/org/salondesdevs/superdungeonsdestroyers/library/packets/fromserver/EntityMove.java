package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntityMove extends Packet {

    public int entityId;
    public Facing facing;

    public EntityMove() {
    }

    public EntityMove(int entityId, Facing facing) {
        this.entityId = entityId;
        this.facing = facing;
    }

    @Override
    public void read(ByteBuf in) {
        this.entityId = in.readInt();
        this.facing = readEnum(Facing.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.entityId);
        writeEnum(this.facing, out);
    }
}
