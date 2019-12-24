package org.salondesdevs.superdungeonsdestroyers.library.components;

import net.wytrem.ecs.*;

/**
 * Represents the position of an entity, <bold>in grid coordinates</bold>.
 */
public class Position implements Component {

    public int x, y;

    public Position() {
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(int x, int y) {
        setX(x);
        setY(y);
    }

    public void increment(Facing facing) {
        this.x += facing.x;
        this.y += facing.y;
    }
}
