package org.salondesdevs.superdungeonsdestroyers.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.utils.TiledMapWrapper;

import javax.inject.Singleton;

@Singleton
public class AssetService extends Service {
    public Texture img;
    public TiledMapWrapper testMap;
    public Texture tilesetTexture;

    @Override
    public void initialize() {
        img = new Texture("badlogic.jpg");
        testMap = this.loadTiledMap("testmap.tmx");
        tilesetTexture = new Texture("dungeon_tileset.png");
    }

    @Override
    public void dispose() {
        img.dispose();
        testMap.dispose();
        tilesetTexture.dispose();
    }

    protected TiledMapWrapper loadTiledMap(String internalName) {
        return new TiledMapWrapper(new TmxMapLoader().load(internalName));
    }
}