package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.utils.TiledMapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class MapLayerRenderer {
    private static final Logger logger = LoggerFactory.getLogger( MapLayerRenderer.class );

    @Inject
    CameraSystem cameraService;

    @Inject
    Assets assets;

    public MapLayerRenderer() {
        renderers = new HashMap<>();
        layers = new HashMap<>();
    }

    public void initialize(String... layersToRender) {
        this.createAndStoreRenderer(assets.testMap, layersToRender);
        for (TiledMap tiledMap : assets.rooms) {
            this.createAndStoreRenderer(tiledMap, layersToRender);
        }
    }

    private void createAndStoreRenderer(TiledMap tiledMap, String... layersToRender) {
        TiledMapRenderer tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, GridSpriteBatch.GRID_SIZE / 16.0f);

        IntList list = new IntArrayList();

        Array<MapLayer> allLayers = TiledMapUtils.getAllLayers(tiledMap);

        for (int i = 0; i < allLayers.size; i++) {
            MapLayer layer = allLayers.get(i);

            if (Arrays.binarySearch(layersToRender, layer.getName()) >= 0) {
                list.add(i);
            }
        }

        logger.debug("Creating renderer for {} with layers {} = {}", tiledMap, Arrays.toString(layersToRender), list.toArray());

        this.layers.put(tiledMap, list.toIntArray());
        this.renderers.put(tiledMap, tiledMapRenderer);
    }

    private Map<TiledMap, TiledMapRenderer> renderers;
    private Map<TiledMap, int[]> layers;

    public void render(TiledMap terrain) {
        TiledMapRenderer tiledMapRenderer = this.renderers.get(terrain);

        if (tiledMapRenderer == null) {
            throw new IllegalStateException("Renderer not registered for map " + terrain);
        }

        tiledMapRenderer.setView(cameraService.camera);
        tiledMapRenderer.render(this.layers.get(terrain));
    }
}
