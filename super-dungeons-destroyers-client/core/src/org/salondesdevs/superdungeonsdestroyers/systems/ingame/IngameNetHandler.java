package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import SDD.Server.Event;
import SDD.Server.EventUnion;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Me;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.components.Size;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.components.TilePosition;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class IngameNetHandler implements NetworkHandlerSystem.Handler {

    private static final Logger logger = LoggerFactory.getLogger( IngameNetHandler.class );

    @Override
    public void handle(Event message) {
        logger.info("Received event with type={}", EventUnion.name(message.eventType()));

        switch (message.eventType()) {
//            case Content
//                    .Environment:
//                this.handleEnvironment((Environment) message.content(new Environment()));
//                break;
//            case Content.Pong:
//                this.handlePong((Pong) message.content(new Pong()));
//                break;
        }
    }

//    private void handlePong(Pong content) {
//        System.err.println("received pong fram ingamenethandler" + content.value());
//    }

    @Inject
    LevelSwitcher mapSwitcher;

    @Inject
    Mapper<TilePosition> positionMapper;

    @Inject
    Mapper<Camera> cameraMapper;

    @Inject
    Mapper<Animated> animatedMapper;

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    Assets assetService;

    @Inject
    Mapper<Size> sizeMapper;

    @Inject
    World world;

    @Inject
    Mapper<Me> meMapper;

    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;

//    public void handleEnvironment(Environment environment) {
//        logger.trace("Received environment {}", environment);
//
//        int me = (int) environment.me();
//
//        // For testing purposes
//        if (!cameraMapper.has(me)) {
//            int terrain = -1;
//            terrainMapper.set(terrain, new Terrain(assetService.testMap));
//
//            int playerTest = world.createEntity();
//
//            Texture walkSheet = assetService.player;
//
//            TextureRegion[][] tmp = TextureRegion.split(walkSheet,
//                    walkSheet.getWidth() / FRAME_COLS,
//                    walkSheet.getHeight() / FRAME_ROWS);
//
//            // Place the regions into a 1D array in the correct order, starting from the top
//            // left, going across first. The Animation constructor requires a 1D array.
//            TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
//            int index = 0;
//            for (int i = 0; i < FRAME_ROWS; i++) {
//                for (int j = 0; j < FRAME_COLS; j++) {
//                    walkFrames[index++] = tmp[i][j];
//                }
//            }
//
//            // Initialize the Animation with the frame interval and array of frames
//            Animation<TextureRegion> walkAnimation = new Animation<>(0.07f, walkFrames);
//
//            animatedMapper.set(playerTest, new Animated(walkAnimation));
//
//            TilePosition pos = new TilePosition(1, 1);
//
//            positionMapper.set(playerTest, pos);
//
//            cameraMapper.set(playerTest, Camera.INSTANCE);
//            meMapper.set(playerTest, Me.INSTANCE);
//
//            offsetMapper.set(playerTest, new Offset());
//
//            sizeMapper.set(playerTest, new Size(15, 20));
//        }
//
//        Level level = environment.level();
//        int kind = level.kind();
//        mapSwitcher.scheduleChange(kind);
//
//
//        for (int i = 0; i < environment.entitiesLength(); i++) {
//            Entity entity = environment.entities(i);
//            int id = (int) entity.entityId();
//
//            if (entity.kindType() == EntityKind.Player) {
//                Player playerMessage = (Player) entity.kind(new Player());
//                if (positionMapper.has(id)) {
//                    positionMapper.get(id).set(playerMessage.location().x(), mapSwitcher.currentHeight - playerMessage.location().y() - 1);
//                }
//                else {
//                    positionMapper.set(id, new TilePosition(playerMessage.location().x(), mapSwitcher.currentHeight - playerMessage.location().y()));
//                }
//            }
//        }
//    }
}
