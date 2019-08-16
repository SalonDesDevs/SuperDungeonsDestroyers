package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Me;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.components.Sprited;
import org.salondesdevs.superdungeonsdestroyers.library.components.Size;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityCreator extends Service {

    @Inject
    Mapper<Sprited> spritedMapper;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Assets assets;

    @Inject
    Mapper<Size> sizeMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    Mapper<Animated> animatedMapper;

    @Inject
    Mapper<Me> meMapper;

    @Inject
    Mapper<Camera> cameraMapper;

    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;

    public void addSprited(int entity, Sprited.Sprites sprites) {
        this.spritedMapper.set(entity, new Sprited(sprites.toTextureRegion(assets.tileset)));
    }

    public void addBasics(int entity) {
        offsetMapper.set(entity, new Offset());
        sizeMapper.set(entity, new Size(15, 20));
        positionMapper.set(entity, new Position(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    public void addRemotePlayer(int entity) {
        addBasics(entity);
        addWalkAnim(entity, assets.otherPlayer);
    }

    public void addLocalPlayer(int me) {
        addBasics(me);
        cameraMapper.set(me, Camera.INSTANCE);
        meMapper.set(me, Me.INSTANCE);

        addWalkAnim(me, assets.player);
    }

    public void addWalkAnim(int entity, Texture walkSheet) {
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        Animation<TextureRegion> walkAnimation = new Animation<>(0.07f, walkFrames);

        animatedMapper.set(entity, new Animated(walkAnimation));
    }

}
