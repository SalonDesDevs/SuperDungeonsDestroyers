package org.salondesdevs.superdungeonsdestroyers.systems;

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.utils.TiledMapUtils;

import javax.inject.Inject;
import java.util.Arrays;

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
    }

    TiledMapRenderer tiledMapRenderer;
    int[] layers;

    @Override
    public void process(int entity) {
        Terrain terrain = terrainMapper.get(entity);

        // TODO: this is ugly for debug proposes
        if (tiledMapRenderer == null) {
            tiledMapRenderer = new OrthogonalTiledMapRenderer(terrain.tiledMap);
            layers = new int[layersToRender.length];
            int index = 0;

            Array<TiledMapTileLayer> allLayers = TiledMapUtils.getLayers(terrain.tiledMap);

            for (int i = 0; i < allLayers.size; i++) {
                TiledMapTileLayer layer = allLayers.get(i);
                if (Arrays.binarySearch(layersToRender, layer.getName()) >= 0) {
                    layers[index] = i;
                    index++;
                }
            }
        }

        tiledMapRenderer.setView(cameraService.camera);
        tiledMapRenderer.render(layers);
    }
}
