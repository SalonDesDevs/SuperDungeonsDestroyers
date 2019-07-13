package org.salondesdevs.superdungeonsdestroyers.components;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.utils.TiledMapWrapper;

public class Terrain implements Component {
    public final TiledMapWrapper tiledMapWrapper;

    public Terrain(TiledMapWrapper tiledMap) {
        this.tiledMapWrapper = tiledMap;
    }
}
