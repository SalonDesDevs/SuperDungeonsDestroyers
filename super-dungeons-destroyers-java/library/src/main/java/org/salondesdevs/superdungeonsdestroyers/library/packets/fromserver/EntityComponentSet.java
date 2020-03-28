package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import net.wytrem.ecs.Component;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.AutoWatched;

public class EntityComponentSet extends EntityPacket {

    public AutoWatched watchableComponent;

    public EntityComponentSet() {

    }

    public EntityComponentSet(int entityId, AutoWatched watchableComponent) {
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("watchableComponent", watchableComponent)
                .add("entityId", getEntity())
                .toString();
    }
}
