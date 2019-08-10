package org.salondesdevs.superdungeonsdestroyers.systems.common;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import net.wytrem.ecs.*;

import javax.inject.Singleton;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Singleton
/**
 * The collection of all assets needed in the game.
 * Fields annotated with {@link Assets.Asset} will be automatically filled
 * up by the {@link org.salondesdevs.superdungeonsdestroyers.systems.loadingassets.AssetsLoadingSystem}.
 *
 * To register an asset, simply create a public field and annotate it with {@link Assets.Asset}.
 */
public class Assets extends Service {

    public static final String ROOMS_FOLDER = "rooms/";
    @Asset(path = "title.png")
    public Texture title;

    @Asset(path = "testmap.tmx")
    public TiledMap testMap;

    @Asset(path = "tiledmaps/dungeon_combined.png")
    public Texture tileset;

    @Asset(path = "player.png")
    public Texture player;

    @Asset(path = ROOMS_FOLDER + "bottom.tmx")
    public TiledMap bottom;

    @Asset(path = ROOMS_FOLDER + "top.tmx")
    public TiledMap top;

    @Asset(path = ROOMS_FOLDER + "cave.tmx")
    public TiledMap cave;

    @Asset(path = ROOMS_FOLDER + "collisions_tester.tmx")
    public TiledMap collisionsTester;

    public TiledMap[] rooms;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Asset {
        String path();

        Class<?> type() default UseFieldType.class;
    }

    /**
     * A type class used to inform the {@link org.salondesdevs.superdungeonsdestroyers.systems.loadingassets.AssetsLoadingSystem}
     * to use the Field's type.
     */
    public static final class UseFieldType {
    }
}