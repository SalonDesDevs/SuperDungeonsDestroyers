package org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.Direction;
import org.salondesdevs.superdungeonsdestroyers.library.packets.ByteBufHelper;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class PlayerMove extends Packet {
    public Direction direction;

    public PlayerMove() {
    }

    public PlayerMove(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void read(ByteBuf in) {
        this.direction = ByteBufHelper.readEnum(Direction.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufHelper.writeEnum(direction, out);
    }
}
