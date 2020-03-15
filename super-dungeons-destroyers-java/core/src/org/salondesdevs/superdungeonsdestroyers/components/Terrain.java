package org.salondesdevs.superdungeonsdestroyers.components;

import com.badlogic.gdx.maps.tiled.TiledMap;

import net.wytrem.ecs.Component;

public class Terrain implements Component {
    public TiledMap tiledMap;

    public Terrain(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }
}
