package org.salondesdevs.superdungeonsdestroyers.systems;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * TO BE DELETED.
 */
@Singleton
public class DebugSystem extends BaseSystem {
    boolean init = false;

    @Inject
    Mapper<Terrain> terrainMapper;

    @Override
    public void process() {
        if (!init) {
            init = true;
            int terrain = world.createEntity();
            // TODO: load Tiled map
            terrainMapper.set(terrain, new Terrain(null));
        }
    }
}
