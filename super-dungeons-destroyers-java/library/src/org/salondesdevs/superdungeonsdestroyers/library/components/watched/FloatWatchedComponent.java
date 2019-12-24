package org.salondesdevs.superdungeonsdestroyers.library.components.watched;

import io.netty.buffer.ByteBuf;
import net.wytrem.ecs.Component;

public class FloatWatchedComponent extends WatchableComponent {

    private float value;

    public FloatWatchedComponent() {
    }

    public FloatWatchedComponent(float value) {
        this.value = value;
    }

    public float get() {
        return this.value;
    }

    public void set(float value) {
        this.value = value;
        this.setChanged();
    }

    @Override
    public void read(ByteBuf in) {
        this.value = in.readFloat();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeFloat(this.value);
    }
}
