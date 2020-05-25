package org.salondesdevs.superdungeonsdestroyers.server.utils;

import org.mapeditor.core.MapLayer;

/**
 * Wrapper class for {@link org.mapeditor.core.Map}. Provides some convenient methods such as {@link MapWrapper#getLayerByName(String)}.
 */
public class MapWrapper {

    private org.mapeditor.core.Map tiledMap;

    public MapWrapper(org.mapeditor.core.Map tiledMap) {
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
