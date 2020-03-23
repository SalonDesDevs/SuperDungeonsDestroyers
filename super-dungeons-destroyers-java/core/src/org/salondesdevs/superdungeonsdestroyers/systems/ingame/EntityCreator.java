package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.components.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Size;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.Welcome;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.PacketReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityCreator extends Service {

    private static final Logger logger = LoggerFactory.getLogger(EntityCreator.class);

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


    public void setSprited(int entity, Sprited.Sprites sprites) {
        this.spritedMapper.set(entity, new Sprited(sprites.toTextureRegion(assets.tileset)));
    }

    public void setBasics(int entity) {
        offsetMapper.set(entity, new Offset());
    }

    public void setPlayer(int entity) {
        setBasics(entity);
        setWalkAnim(entity, 608, 74);
    }

    public void addLocalPlayer(int me) {
        cameraMapper.set(me, Camera.INSTANCE);
        meMapper.set(me, Me.INSTANCE);

        setWalkAnim(me, 608, 74);
    }

    private static final int WIDTH = 128;
    private static final int HEIGHT = 22;
    private static final int FRAME_COLS = 8, FRAME_ROWS = 1;

    public void setWalkAnim(int entity, int u, int v) {
        TextureRegion walkSheet = new TextureRegion(assets.tileset, u, v, WIDTH, HEIGHT);
        TextureRegion[][] tmp = walkSheet.split(WIDTH / FRAME_COLS, HEIGHT / FRAME_ROWS);

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

    @EventHandler
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof Welcome) {
            addLocalPlayer(((Welcome) packetReceivedEvent.getPacket()).me);
        }
    }
}
