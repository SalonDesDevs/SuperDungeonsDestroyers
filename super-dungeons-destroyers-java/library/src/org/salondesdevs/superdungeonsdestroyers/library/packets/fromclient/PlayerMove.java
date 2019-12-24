package org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class PlayerMove extends Packet {
    public Facing facing;

    public PlayerMove() {
    }

    public PlayerMove(Facing facing) {
        this.facing = facing;
    }

    @Override
    public void read(ByteBuf in) {
        this.facing = readEnum(Facing.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        writeEnum(facing, out);
    }
}
