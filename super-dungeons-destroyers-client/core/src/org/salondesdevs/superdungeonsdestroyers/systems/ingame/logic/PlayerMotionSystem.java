package org.salondesdevs.superdungeonsdestroyers.systems.ingame.logic;

import SDD.Common.Direction;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.TimeUtils;
import com.google.common.eventbus.Subscribe;
import it.unimi.dsi.fastutil.ints.Int2ByteArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ByteMap;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.components.TilePosition;
import org.salondesdevs.superdungeonsdestroyers.events.input.KeyPressedEvent;
import org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Animation;
import org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerMotionSystem extends Service {

    private Int2ByteMap keysToDirection = new Int2ByteArrayMap();

    @Inject
    NetworkSystem networkSystem;

    @Inject
    Mapper<TilePosition> positionMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    Animator animator;

    @Inject
    PlayerIdHolder playerIdHolder;

    private long lastMoved = 0L;

    private static final long delay = 300L;

    @Override
    public void initialize() {
        keysToDirection.put(Input.Keys.UP, Direction.Up);
        keysToDirection.put(Input.Keys.DOWN, Direction.Down);
        keysToDirection.put(Input.Keys.RIGHT, Direction.Right);
        keysToDirection.put(Input.Keys.LEFT, Direction.Left);
    }

    @Subscribe
    public void keyPressed(KeyPressedEvent keyPressedEvent) {

        if (keysToDirection.keySet().contains(keyPressedEvent.getKeycode())) {
            long now = TimeUtils.millis();

            if (now - lastMoved < delay) {
                return;
            }
            lastMoved = now;

            byte direction = keysToDirection.get(keyPressedEvent.getKeycode());


            TilePosition tilePosition = positionMapper.get(playerIdHolder.getEntityId());
            Offset offset = offsetMapper.get(playerIdHolder.getEntityId());

            switch (direction) {
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


            networkSystem.request().addMoveContent(direction).writeAndFlush();

            Animation<Float> walkAnimation = animator.createMoveAnimation(offset, direction);

            animator.play(walkAnimation);
        }
    }
}
