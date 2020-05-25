package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Levels;
import org.salondesdevs.superdungeonsdestroyers.content.Assets;
import org.salondesdevs.superdungeonsdestroyers.utils.TiledMapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelSwitcher extends Service {

    private static final Logger logger = LoggerFactory.getLogger( LevelSwitcher.class );

    @Inject
    Assets assets;

    public int currentHeight;

    int current = Integer.MIN_VALUE;
    TiledMap currentMap = null;

    public void setRoom(Levels room) {
        current = room.ordinal();
        this.currentMap = assets.rooms[this.current];
        this.currentHeight = TiledMapUtils.getHeight(this.currentMap);
    }

    public TiledMap getTerrain() {
        return this.currentMap;
    }

    public TiledMapTileLayer getGround() {
        return (TiledMapTileLayer) this.currentMap.getLayers().get("ground");
    }
}
