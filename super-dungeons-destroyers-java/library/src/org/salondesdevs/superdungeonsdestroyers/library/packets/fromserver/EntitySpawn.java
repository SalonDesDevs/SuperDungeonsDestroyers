package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntitySpawn extends EntityPacket {
    public EntityKind entityKind;

    public EntitySpawn() {
    }

    public EntitySpawn(int entityId, EntityKind entityKind) {
        super(entityId);
        this.entityKind = entityKind;
    }

    @Override
    public void read(ByteBuf in) {
        super.read(in);
        this.entityKind = readEnum(EntityKind.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        super.write(out);
        writeEnum(this.entityKind, out);
    }
}
