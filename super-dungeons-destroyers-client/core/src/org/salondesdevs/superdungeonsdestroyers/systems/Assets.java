package org.salondesdevs.superdungeonsdestroyers.systems;

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
 * Fields annotated with {@link Assets.Asset} will be automatically filled up by the {@link org.salondesdevs.superdungeonsdestroyers.systems.loadingassets.AssetsLoadingSystem}.
 *
 */
public class Assets extends Service {

    @Asset(path = "badlogic.jpg")
    public Texture img;

    @Asset(path = "testmap.tmx")
    public TiledMap testMap;

    @Asset(path = "dungeon_tileset.png")
    public Texture tilesetTexture;

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
    public static final class UseFieldType {}
}