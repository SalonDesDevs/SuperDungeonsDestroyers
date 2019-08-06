package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import SDD.Common.Direction;
import SDD.Common.LevelEnvironment;
import SDD.Server.EntityMove;
import SDD.Server.EntityTeleport;
import SDD.Server.Event;
import SDD.Server.EventUnion;
import SDD.Server.Welcome;
import SDD.Server.ZoneInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Me;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.components.Size;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.components.TilePosition;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Animator;
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
            case EventUnion.Welcome:
                this.handleWelcome((Welcome) message.event(new Welcome()));
                break;
            case EventUnion.ZoneInfo:
                this.handleZoneInfo((ZoneInfo) message.event(new ZoneInfo()));
                break;
            case EventUnion.EntityMove:
                this.handleEntityMove((EntityMove) message.event(new EntityMove()));
                break;
            case EventUnion.EntityTeleport:
                this.handleEntityTeleport((EntityTeleport) message.event(new EntityTeleport()));
                break;
        }
    }



    private void handleEntityTeleport(EntityTeleport event) {
        int entityId = (int) event.entityId();
        if (positionMapper.has(entityId)) {
            positionMapper.get(entityId).set(event.location().x(), mapSwitcher.currentHeight - event.location().y() - 1);
        }
    }

    @Inject
    Animator animator;

    private void handleEntityMove(EntityMove event) {
        int entityId = (int) event.entityId();
        if (positionMapper.has(entityId) && offsetMapper.has(entityId)) {

            TilePosition tilePosition = positionMapper.get(entityId);
            Offset offset = offsetMapper.get(entityId);

            switch (event.direction()) {
                case Direction.Up:
                    tilePosition.y++;
                    break;
                case Direction.Down:
                    tilePosition.y--;
                    break;
                case Direction.Left:
                    tilePosition.x--;
                    break;
                case Direction.Right:
                    tilePosition.x++;
                    break;
            }

            org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Animation<Float> walkAnimation = animator.createMoveAnimation(offset, event.direction(), () -> {});
            animator.play(walkAnimation);
        }
    }

    private void handleZoneInfo(ZoneInfo zoneInfo) {
        int kind = zoneInfo.environment();
        mapSwitcher.scheduleChange(kind);
    }

    private void handleWelcome(Welcome welcome) {
        int me = (int) welcome.me().entityId();

        // For testing purposes
        if (!cameraMapper.has(me)) {
            int terrain = -1;
            terrainMapper.set(terrain, new Terrain(assetService.testMap));

            mapSwitcher.scheduleChange(LevelEnvironment.Bottom);

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
            Animation<TextureRegion> walkAnimation = new Animation<>(0.07f, walkFrames);

            animatedMapper.set(playerTest, new Animated(walkAnimation));

            TilePosition pos = new TilePosition(1, 1);

            positionMapper.set(playerTest, pos);

            cameraMapper.set(playerTest, Camera.INSTANCE);
            meMapper.set(playerTest, Me.INSTANCE);

            offsetMapper.set(playerTest, new Offset());

            sizeMapper.set(playerTest, new Size(15, 20));
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
//

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
