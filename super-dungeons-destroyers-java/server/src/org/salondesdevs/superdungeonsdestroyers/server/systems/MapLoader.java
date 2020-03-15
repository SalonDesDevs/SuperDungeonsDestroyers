package org.salondesdevs.superdungeonsdestroyers.server.systems;

import java.io.IOException;

import org.mapeditor.core.Map;
import org.mapeditor.io.MapReader;

import net.wytrem.ecs.Service;

public class MapLoader extends Service {
    @Override
    public void initialize() {
        MapReader mapReader = new MapReader();
        try {
            Map map = mapReader.readMap("rooms/top.tmx");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
