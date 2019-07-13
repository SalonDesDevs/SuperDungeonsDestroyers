package org.salondesdevs.superdungeonsdestroyers.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

public class TiledMapWrapper {

    private final TiledMap tiledMap;
    private final int width, height, tileWidth, tileHeight;

    private final Array<TiledMapTileLayer> layers;

    public TiledMapWrapper(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        width = tiledMap.getProperties().get("width", Integer.class);
        height = tiledMap.getProperties().get("height", Integer.class);
        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        layers = tiledMap.getLayers().getByType(TiledMapTileLayer.class);
    }

    public void dispose() {
        this.tiledMap.dispose();
    }

    public MapMask createMapMask(String property) {
        return new MapMask(height, width, tileWidth, tileHeight, layers, property);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public Array<TiledMapTileLayer> getLayers() {
        return layers;
    }
}
