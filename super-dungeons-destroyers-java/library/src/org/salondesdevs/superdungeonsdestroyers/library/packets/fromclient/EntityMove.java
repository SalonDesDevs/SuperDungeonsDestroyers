package org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.Direction;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntityMove extends Packet {
    public Direction direction;

    public EntityMove() {
    }

    public EntityMove(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void read(ByteBuf in) {
        this.direction = Direction.values()[in.readByte()];
    }

    @Override
    public void write(ByteBuf out) {
        out.writeByte(this.direction.ordinal());
    }
}
