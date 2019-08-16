package org.salondesdevs.superdungeonsdestroyers.library.components;

import net.wytrem.ecs.*;

public class Size implements Component {

    public float width, height;

    public Size(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
