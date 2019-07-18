package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.utils.TiledMapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * If a {@link org.salondesdevs.superdungeonsdestroyers.components.Terrain} entity is present, will use it to render the
 * specified map layers.
 */
public abstract class MapLayerRenderSystem extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger( MapLayerRenderSystem.class );

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    CameraSystem cameraService;

    @Inject
    Assets assets;

    private String[] layersToRender;

    public MapLayerRenderSystem(String... layers) {
        super(Aspect.all(Terrain.class));
        this.layersToRender = layers;
        renderers = new HashMap<>();
        this.layers = new HashMap<>();
    }

    @Override
    public void initialize() {
        this.createAndStoreRenderer(assets.testMap);
        for (TiledMap tiledMap : assets.rooms) {
            this.createAndStoreRenderer(tiledMap);
        }
    }

    private void createAndStoreRenderer(TiledMap tiledMap) {
        TiledMapRenderer tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        IntList list = new IntArrayList();

        Array<TiledMapTileLayer> allLayers = TiledMapUtils.getLayers(tiledMap);

        for (int i = 0; i < allLayers.size; i++) {
            TiledMapTileLayer layer = allLayers.get(i);

            if (Arrays.binarySearch(layersToRender, layer.getName()) >= 0) {
                list.add(i);
            }
        }


        logger.info("Creating renderer for {} with layers {} = {}", tiledMap, Arrays.toString(this.layersToRender), list.toArray());

        this.layers.put(tiledMap, list.toIntArray());
        this.renderers.put(tiledMap, tiledMapRenderer);
    }

    private Map<TiledMap, TiledMapRenderer> renderers;
    private Map<TiledMap, int[]> layers;

    @Override
    public void process(int entity) {
        Terrain terrain = terrainMapper.get(entity);
        this.render(terrain);
    }

    public void render(Terrain terrain) {
        TiledMapRenderer tiledMapRenderer = this.renderers.get(terrain.tiledMap);

        if (tiledMapRenderer == null) {
            throw new IllegalStateException("Renderer not registered for map " + terrain.tiledMap);
        }

        logger.info("rendering {}={}", Arrays.toString(this.layersToRender), this.layers.get(terrain.tiledMap));

        tiledMapRenderer.setView(cameraService.camera);
        tiledMapRenderer.render(this.layers.get(terrain.tiledMap));
    }
}
