package org.salondesdevs.superdungeonsdestroyers.library.components;

import org.salondesdevs.superdungeonsdestroyers.library.components.watched.WatchableComponent;

import io.netty.buffer.ByteBuf;

/**
 * Represents the display Size of an entity, <bold>in grid coordinates</bold>.
 */
public class Size extends WatchableComponent {

    private float width;
    private float height;

    public Size() {
    }

    public Size(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setWidth(float width) {
        if (width != this.width) {
            setChanged();
        }
        this.width = width;
    }

    public void setHeight(float height) {
        if (height != this.height) {
            this.setChanged();
        }
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void set(float width, float height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    @Override
    public void read(ByteBuf in) {
        this.width = in.readFloat();
        this.height = in.readFloat();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeFloat(this.width);
        out.writeFloat(this.height);
    }
}
