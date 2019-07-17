package org.salondesdevs.superdungeonsdestroyers.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Position;
import org.salondesdevs.superdungeonsdestroyers.components.Sprited;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.AnimatedSpriteRenderer;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.CameraService;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.CameraSyncSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.EntityCreator;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.GroundRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.IngameNetHandler;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.InputSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.MapSwitcher;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.OverlayRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.SpriteRenderer;

import javax.inject.Inject;

public class IngameState extends GameState {

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    Assets assetService;

    @Inject
    World world;
    @Inject
    NetworkHandlerSystem networkHandlerSystem;


    public IngameState() {
        super();

        // Be aware, the order matters.
        register(Assets.class);
        register(NetworkSystem.class);
        register(NetworkHandlerSystem.class);
        register(MapSwitcher.class);
        register(CameraService.class);
        register(CameraSyncSystem.class);
        register(ClearScrenSystem.class);
        register(GroundRenderSystem.class);
        register(SpriteRenderer.class);
        register(AnimatedSpriteRenderer.class);
        register(OverlayRenderSystem.class);
        register(InputSystem.class);
    }

    @Inject
    EntityCreator entityCreator;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Mapper<Camera> cameraMapper;

    @Inject
    Mapper<Animated> animatedMapper;

    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;
    @Override
    public void pushed() {
        this.networkHandlerSystem.setCurrentHandler(IngameNetHandler.class);

        int terrain = -1;
        terrainMapper.set(terrain, new Terrain(assetService.testMap));

        int playerTest = world.createEntity();

        Texture walkSheet = assetService.player;

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
        Animation<TextureRegion> walkAnimation = new Animation<>(0.025f, walkFrames);

//        entityCreator.addSprited(playerTest, Sprited.Sprites.MAGE);

        animatedMapper.set(playerTest, new Animated(walkAnimation));

        Position pos = new Position();
        pos.x = pos.y = 1;

        positionMapper.set(playerTest, pos);

        cameraMapper.set(playerTest, Camera.INSTANCE);
    }


}
