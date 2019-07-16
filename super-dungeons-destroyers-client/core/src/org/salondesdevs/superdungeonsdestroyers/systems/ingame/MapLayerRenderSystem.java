package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.utils.TiledMapUtils;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * If a {@link org.salondesdevs.superdungeonsdestroyers.components.Terrain} entity is present, will use it to render the
 * specified map layers.
 */
public abstract class MapLayerRenderSystem extends IteratingSystem {

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    CameraService cameraService;

    private String[] layersToRender;

    public MapLayerRenderSystem(String... layers) {
        super(Aspect.all(Terrain.class));
        this.layersToRender = layers;
        renderers = new HashMap<>();
        this.layers = new HashMap<>();
    }

    private Map<TiledMap, TiledMapRenderer> renderers;
    private Map<TiledMap, int[]> layers;

    @Override
    public void process(int entity) {
        Terrain terrain = terrainMapper.get(entity);

        TiledMapRenderer tiledMapRenderer = this.renderers.get(terrain.tiledMap);

        // TODO: this is ugly for debug proposes
        if (tiledMapRenderer == null) {
            tiledMapRenderer = new OrthogonalTiledMapRenderer(terrain.tiledMap);
            int[] array = new int[layersToRender.length];
            int index = 0;

            Array<TiledMapTileLayer> allLayers = TiledMapUtils.getLayers(terrain.tiledMap);

            for (int i = 0; i < allLayers.size; i++) {
                TiledMapTileLayer layer = allLayers.get(i);
                if (Arrays.binarySearch(layersToRender, layer.getName()) >= 0) {
                    array[index] = i;
                    index++;
                }
            }

            this.layers.put(terrain.tiledMap, array);
        }

        tiledMapRenderer.setView(cameraService.camera);
        tiledMapRenderer.render(this.layers.get(terrain.tiledMap));
    }
}
