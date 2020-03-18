package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;

import io.netty.buffer.ByteBuf;

public class EntityMove extends EntityPacket {

    public Facing facing;

    public EntityMove() {
    }

    public EntityMove(int entityId, Facing facing) {
        super(entityId);
        this.facing = facing;
    }

    @Override
    public void read(ByteBuf in) {
        super.read(in);
        this.facing = readEnum(Facing.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        super.write(out);
        writeEnum(this.facing, out);
    }
}
