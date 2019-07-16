package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapSwitcher extends IteratingSystem {

    int scheduledRoom = -1;

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    Assets assets;

    public MapSwitcher() {
        super(Aspect.all(Terrain.class));
    }

    @Override
    public void process(int entity) {
        if (scheduledRoom != -1) {

            Terrain terrain = terrainMapper.get(entity);
            terrain.tiledMap = assets.rooms[scheduledRoom];
            scheduledRoom = -1;
        }
    }

    public void scheduleChange(int room) {
        this.scheduledRoom = room;
    }
}
