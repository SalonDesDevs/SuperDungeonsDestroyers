package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * If a {@link org.salondesdevs.superdungeonsdestroyers.components.Terrain} entity is present, will use it to render the
 * map layer called "ground".
 */
@Singleton
public class GroundRenderer extends IteratingSystem {

    @Inject
    MapLayerRenderer mapLayerRenderer;

    public GroundRenderer() {
        super(Aspect.all(Terrain.class));
    }

    @Override
    public void initialize() {
        mapLayerRenderer.initialize("ground");
    }

    @Inject
    Mapper<Terrain> terrainMapper;

    @Override
    public void process(int entity) {
        this.mapLayerRenderer.render(this.terrainMapper.get(entity));
    }
}
