package org.salondesdevs.superdungeonsdestroyers.systems;

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;

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
            tiledMapRenderer = new OrthogonalTiledMapRenderer(terrain.tiledMapWrapper.getTiledMap());
            layers = new int[layersToRender.length];
            int index = 0;

            for (int i = 0; i < terrain.tiledMapWrapper.getLayers().size; i++) {
                TiledMapTileLayer layer = terrain.tiledMapWrapper.getLayers().get(i);
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
