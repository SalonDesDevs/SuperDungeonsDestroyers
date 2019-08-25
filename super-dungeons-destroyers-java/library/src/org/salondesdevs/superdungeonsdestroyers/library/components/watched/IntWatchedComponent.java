package org.salondesdevs.superdungeonsdestroyers.library.components.watched;

import io.netty.buffer.ByteBuf;

public class IntWatchedComponent extends WatchableComponent {

    private int value;

    public IntWatchedComponent() {

    }

    public IntWatchedComponent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.setChanged();
    }

    @Override
    public void read(ByteBuf in) {
        this.value = in.readInt();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.value);
    }
}
