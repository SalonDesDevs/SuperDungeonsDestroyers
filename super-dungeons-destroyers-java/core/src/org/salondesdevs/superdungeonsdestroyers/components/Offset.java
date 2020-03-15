package org.salondesdevs.superdungeonsdestroyers.components;

import net.wytrem.ecs.Component;

public class Offset implements Component {

    public float x, y;

    public Offset() {

    }

    public Offset(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        setX(x);
        setY(y);
    }
}
