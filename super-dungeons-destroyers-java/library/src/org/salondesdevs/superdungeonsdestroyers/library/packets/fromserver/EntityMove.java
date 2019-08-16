package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.Direction;
import org.salondesdevs.superdungeonsdestroyers.library.packets.ByteBufHelper;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntityMove extends Packet {

    public int entityId;
    public Direction direction;

    public EntityMove() {
    }

    public EntityMove(int entityId, Direction direction) {
        this.entityId = entityId;
        this.direction = direction;
    }

    @Override
    public void read(ByteBuf in) {
        this.entityId = in.readInt();
        this.direction = ByteBufHelper.readEnum(Direction.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.entityId);
        ByteBufHelper.writeEnum(this.direction, out);
    }
}
