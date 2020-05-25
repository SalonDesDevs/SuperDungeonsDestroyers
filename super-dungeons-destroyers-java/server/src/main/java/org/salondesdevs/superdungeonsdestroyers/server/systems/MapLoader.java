package org.salondesdevs.superdungeonsdestroyers.server.systems;

import net.wytrem.ecs.Service;
import org.mapeditor.core.Map;
import org.mapeditor.io.TMXMapReader;
import org.salondesdevs.superdungeonsdestroyers.server.utils.MapWrapper;

import javax.inject.Singleton;

@Singleton
public class MapLoader extends Service {

    MapWrapper map;

    @Override
    public void initialize() {
        TMXMapReader tmxMapReader = new TMXMapReader();
        try {
            Map tiledMap = tmxMapReader.readMap("rooms/top.tmx");
            this.map = new MapWrapper(tiledMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
