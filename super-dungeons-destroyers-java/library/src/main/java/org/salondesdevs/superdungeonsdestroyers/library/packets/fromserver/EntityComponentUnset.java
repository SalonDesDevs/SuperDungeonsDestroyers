package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.AutoWatchedComponents;

public class EntityComponentUnset extends EntityPacket {

    /**
     * The unset component id in the {@link AutoWatchedComponents} registry.
     */
    private byte componentId;

    public EntityComponentUnset() {

    }

    public EntityComponentUnset(int entityId, byte componentId) {
        super(entityId);
        this.componentId = componentId;
    }

    public byte getComponentId() {
        return componentId;
    }

    @Override
    public void read(ByteBuf in) {
        super.read(in);
        this.componentId = in.readByte();
    }

    @Override
    public void write(ByteBuf out) {
        super.write(out);
        out.writeByte(this.componentId);
    }
}
