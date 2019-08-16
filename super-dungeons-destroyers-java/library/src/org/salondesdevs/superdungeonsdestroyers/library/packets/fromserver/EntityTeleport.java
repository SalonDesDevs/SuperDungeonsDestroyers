package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntityTeleport extends Packet {

    public int entityId, x, y;

    public EntityTeleport() {
        super();
    }

    public EntityTeleport(int entityId, int x, int y) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
    }

    public EntityTeleport(int entityId, Position position) {
        this(entityId, position.x, position.y);
    }

    @Override
    public void read(ByteBuf in) {
        this.entityId = in.readInt();
        this.x = in.readInt();
        this.y = in.readInt();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(entityId);
        out.writeInt(x);
        out.writeInt(y);
    }
}
