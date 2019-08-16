package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.packets.ByteBufHelper;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntitySpawn extends Packet {
    public int entityId;
    public EntityKind entityKind;

    public EntitySpawn() {
    }

    public EntitySpawn(int entityId, EntityKind entityKind) {
        this.entityId = entityId;
        this.entityKind = entityKind;
    }

    @Override
    public void read(ByteBuf in) {
        this.entityId = in.readInt();
        this.entityKind = ByteBufHelper.readEnum(EntityKind.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.entityId);
        ByteBufHelper.writeEnum(this.entityKind, out);
    }
}
