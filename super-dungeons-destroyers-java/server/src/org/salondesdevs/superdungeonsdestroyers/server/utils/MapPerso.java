package org.salondesdevs.superdungeonsdestroyers.server.utils;

import org.mapeditor.core.MapLayer;

public class MapPerso {

    private org.mapeditor.core.Map tiledMap;

    public MapPerso(org.mapeditor.core.Map tiledMap) {
        this.tiledMap = tiledMap;
    }

    public MapLayer getLayerByName(String name) {
        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer.getName().equals(name)) {
                return layer;
            }
        }

        return null;
    }

    public org.mapeditor.core.Map getTiledMap() {
        return tiledMap;
    }
}
