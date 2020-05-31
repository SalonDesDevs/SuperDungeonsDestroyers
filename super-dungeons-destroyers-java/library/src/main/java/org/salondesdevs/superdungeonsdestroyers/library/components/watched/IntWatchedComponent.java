package org.salondesdevs.superdungeonsdestroyers.library.components.watched;

import io.netty.buffer.ByteBuf;

public class IntWatchedComponent extends WatchableComponent {

    private int value;

    public IntWatchedComponent() {

    }

    public IntWatchedComponent(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
        this.setChanged();
    }

    public void increment() {
        this.value++;
        this.setChanged();
    }

    public void decrement() {
        this.value--;
        this.setChanged();
    }

    public void add(int a) {
        this.value += a;
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
