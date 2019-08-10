package org.salondesdevs.superdungeonsdestroyers.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

public class TiledMapUtils {

    public static int getWidth(TiledMap tiledMap) {
        return tiledMap.getProperties().get("width", Integer.class);
    }

    public static int getHeight(TiledMap tiledMap) {
        return tiledMap.getProperties().get("height", Integer.class);
    }

    public static int getTileWidth(TiledMap tiledMap) {
        return tiledMap.getProperties().get("tilewidth", Integer.class);
    }

    public static int getTileHeight(TiledMap tiledMap) {
        return tiledMap.getProperties().get("tileheight", Integer.class);
    }

    public static Array<TiledMapTileLayer> getTileLayers(TiledMap tiledMap) {
        return tiledMap.getLayers().getByType(TiledMapTileLayer.class);
    }

    public static Array<MapLayer> getAllLayers(TiledMap tiledMap) {
        return tiledMap.getLayers().getByType(MapLayer.class);
    }

    public static MapMask createMask(TiledMap tiledMap, String propertyString) {
        return new MapMask(tiledMap, propertyString);
    }
}
