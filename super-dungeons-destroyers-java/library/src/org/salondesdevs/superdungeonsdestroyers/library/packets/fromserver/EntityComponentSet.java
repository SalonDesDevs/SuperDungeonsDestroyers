package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import net.wytrem.ecs.Component;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntityComponentSet extends Packet {

    public int entityId;
    public Component watchableComponent;

    public EntityComponentSet() {

    }

    public EntityComponentSet(int entityId, Component watchableComponent) {
        this.entityId = entityId;
        this.watchableComponent = watchableComponent;
    }

    @Override
    public void read(ByteBuf in) {
        this.entityId = in.readInt();
        this.watchableComponent = readWatchableComponent(in);
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.entityId);
        writeWatchableComponent(this.watchableComponent, out);
    }
}
