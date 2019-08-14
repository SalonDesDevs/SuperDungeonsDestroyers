package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.utils.TiledMapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelSwitcher extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger( LevelSwitcher.class );

    int scheduledRoom = -1;

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    Assets assets;

    public int currentHeight;

    int current = Integer.MIN_VALUE;

    public LevelSwitcher() {
        super(Aspect.all(Terrain.class));
    }

    @Override
    public void process(int entity) {
        if (scheduledRoom != -1 && scheduledRoom != current) {
            Terrain terrain = terrainMapper.get(entity);
            terrain.tiledMap = assets.rooms[scheduledRoom];

//            logger.info("Switched level to {}={}", scheduledRoom, LevelEnvironment.name(scheduledRoom));

            current = scheduledRoom;
            scheduledRoom = -1;
        }
    }

    public void scheduleChange(int room) {
        this.scheduledRoom = room;
        this.currentHeight = TiledMapUtils.getHeight(assets.rooms[room]);
    }
}
