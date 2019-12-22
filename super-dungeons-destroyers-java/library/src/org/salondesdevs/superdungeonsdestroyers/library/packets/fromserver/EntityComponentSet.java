package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import net.wytrem.ecs.Component;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntityComponentSet extends EntityPacket {

    public Component watchableComponent;

    public EntityComponentSet() {

    }

    public EntityComponentSet(int entityId, Component watchableComponent) {
        super(entityId);
        this.watchableComponent = watchableComponent;
    }

    @Override
    public void read(ByteBuf in) {
        super.read(in);
        this.watchableComponent = readWatchableComponent(in);
    }

    @Override
    public void write(ByteBuf out) {
        super.write(out);
        writeWatchableComponent(this.watchableComponent, out);
    }
}
