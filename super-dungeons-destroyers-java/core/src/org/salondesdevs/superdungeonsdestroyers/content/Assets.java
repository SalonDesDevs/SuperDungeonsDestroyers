package org.salondesdevs.superdungeonsdestroyers.content;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Levels;

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

    // ---------- Assets from files ----------
    @Asset(path = "title.png")
    public Texture title;

    @Asset(path = "title_background.png")
    public Texture titleBackground;

    @Asset(path = "tiledmaps/dungeon_combined.png")
    public Texture tileset;

    @Asset(path = "player.png")
    public Texture player;

    @Asset(path = "other_player.png")
    public Texture otherPlayer;

    @Asset(path = ROOMS_FOLDER + "bottom.tmx")
    public TiledMap bottom;

    @Asset(path = ROOMS_FOLDER + "top.tmx")
    public TiledMap top;

    @Asset(path = ROOMS_FOLDER + "cave.tmx")
    public TiledMap cave;

    @Asset(path = ROOMS_FOLDER + "collisions_tester.tmx")
    public TiledMap collisionsTester;

    @Asset(path = "tixel/x1/tixel.json")
    public Skin skin;

    @Asset(path = "i18n/all")
    public I18NBundle i18n;

    // ---------- Composite assets ----------

    public TiledMap[] rooms;

    public Animation<TextureRegion> bowmanIdle;
    public Animation<TextureRegion> bowmanWalk;
    public Animation<TextureRegion> mageIdle;
    public Animation<TextureRegion> mageWalk;
    public Animation<TextureRegion> knightIdle;
    public Animation<TextureRegion> knightWalk;

    /**
     * Initializes the assets made entirely from the {@link Asset} annotated fields.
     */
    public void createCompositeAssets() {

        rooms = new TiledMap[Levels.values().length];

        // TODO: load rooms
        rooms[Levels.BOTTOM.ordinal()] = bottom;
        rooms[Levels.CAVE.ordinal()] = cave;
        rooms[Levels.TOP.ordinal()] = top;
        rooms[Levels.COLLISIONS_TESTER.ordinal()] = collisionsTester;

        final int height = 22;
        final int frameRows = 1;
        int v = 42;

        int frameColumns = 2;
        this.bowmanIdle = createAnimationFromTileset(1f, 608, v, 16 * frameColumns, height, frameColumns, frameRows);
        this.bowmanIdle.setPlayMode(Animation.PlayMode.LOOP);
        frameColumns = 6;
        this.bowmanWalk = createAnimationFromTileset(0.1f, 640, v, 16 * frameColumns, height, frameColumns, frameRows);
        this.bowmanWalk.setPlayMode(Animation.PlayMode.LOOP);


        frameColumns = 2;
        v = 74;
        this.knightIdle = createAnimationFromTileset(1f, 608, v, 16 * frameColumns, height, frameColumns, frameRows);
        this.knightIdle.setPlayMode(Animation.PlayMode.LOOP);
        frameColumns = 6;
        this.knightWalk = createAnimationFromTileset(0.1f, 640, v, 16 * frameColumns, height, frameColumns, frameRows);
        this.knightWalk.setPlayMode(Animation.PlayMode.LOOP);

        frameColumns = 2;
        v = 170;
        this.mageIdle = createAnimationFromTileset(1f, 608, v, 16 * frameColumns, height, frameColumns, frameRows);
        this.mageIdle.setPlayMode(Animation.PlayMode.LOOP);
        frameColumns = 6;
        this.mageWalk = createAnimationFromTileset(0.1f, 640, v, 16 * frameColumns, height, frameColumns, frameRows);
        this.mageWalk.setPlayMode(Animation.PlayMode.LOOP);
    }

    private Animation<TextureRegion> createAnimationFromTileset(float frameDuration, int u, int v, int width, int height, int frameColums, int frameRows) {
        TextureRegion walkSheet = new TextureRegion(this.tileset, u, v, width, height);
        TextureRegion[][] tmp = walkSheet.split(width / frameColums, height / frameRows);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[frameColums * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameColums; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        return new Animation<>(frameDuration, walkFrames);
    }

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