package org.salondesdevs.superdungeonsdestroyers.library.components;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.WatchableComponent;

public class Name extends WatchableComponent {
    private String value;

    public Name() {

    }

    public Name(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String name) {
        if (!value.equals(name)) {
            this.value = name;
            this.setChanged();
        }
    }

    @Override
    public void read(ByteBuf in) {
        this.value = readString(in);
    }

    @Override
    public void write(ByteBuf out) {
        writeString(this.value, out);
    }
}
