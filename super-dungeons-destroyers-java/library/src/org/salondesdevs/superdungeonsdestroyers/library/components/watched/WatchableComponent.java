package org.salondesdevs.superdungeonsdestroyers.library.components.watched;

import net.wytrem.ecs.Component;
import org.salondesdevs.superdungeonsdestroyers.library.utils.NettySerializable;

public abstract class WatchableComponent implements Component, NettySerializable {

    private boolean changed;

    public boolean hasChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void setChanged() {
        this.setChanged(true);
    }
}
