package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import org.salondesdevs.superdungeonsdestroyers.library.components.Position;

import io.netty.buffer.ByteBuf;

public class EntityTeleport extends EntityPacket {

    public int x, y;

    public EntityTeleport() {

    }

    public EntityTeleport(int entityId, int x, int y) {
        super(entityId);
        this.x = x;
        this.y = y;
    }

    public EntityTeleport(int entityId, Position position) {
        this(entityId, position.x, position.y);
    }

    @Override
    public void read(ByteBuf in) {
        super.read(in);
        this.x = in.readInt();
        this.y = in.readInt();
    }

    @Override
    public void write(ByteBuf out) {
        super.write(out);
        out.writeInt(x);
        out.writeInt(y);
    }
}
