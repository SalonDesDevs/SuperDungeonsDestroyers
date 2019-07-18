package org.salondesdevs.superdungeonsdestroyers.components;

import net.wytrem.ecs.*;

public class Position implements Component {

    public int x, y;

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
}
