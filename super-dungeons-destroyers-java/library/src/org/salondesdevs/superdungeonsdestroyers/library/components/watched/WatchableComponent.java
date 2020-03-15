package org.salondesdevs.superdungeonsdestroyers.library.components.watched;

import org.salondesdevs.superdungeonsdestroyers.library.utils.NettySerializable;

import net.wytrem.ecs.Component;

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
