package org.salondesdevs.superdungeonsdestroyers.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

/**
 * Creates a mask based on tiles with a certain propertyKey.
 *
 * Useful for creating a mask for collidable parts of the map.
 *
 * @author Daan van Yperen
 */
public class MapMask {

    public final boolean[][] v;
    public final int height;
    public final int width;

    public MapMask(TiledMap tiledMap, String propertyKey) {
        this.height = TiledMapUtils.getHeight(tiledMap);
        this.width = TiledMapUtils.getWidth(tiledMap);
        this.v = new boolean[height][width];
        generate(TiledMapUtils.getTileLayers(tiledMap), propertyKey);
    }

    /**
     * @param x grid coordinates
     * @param y grid coordinates.
     * @return TRUE when property found at TILE coordinates, FALSE if otherwise or out of bounds.
     */
    public boolean atGrid( final int x, final int y, boolean outOfBoundsResult )
    {
        if ( x >= width || x < 0 || y < 0 || y >= height  ) return outOfBoundsResult;
        return v[y][x];
    }

    private void generate(Array<TiledMapTileLayer> layers, String propertyKey) {
        for (TiledMapTileLayer layer : layers) {
            for (int ty = 0; ty < height; ty++) {
                for (int tx = 0; tx < width; tx++) {
                    final TiledMapTileLayer.Cell cell = layer.getCell(tx, ty);
                    if ( cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(propertyKey)) {
                        v[ty][tx] = true;
                    }
                }
            }
        }
    }
}
