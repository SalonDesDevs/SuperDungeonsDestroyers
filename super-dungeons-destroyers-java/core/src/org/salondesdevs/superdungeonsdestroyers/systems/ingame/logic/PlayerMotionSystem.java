package org.salondesdevs.superdungeonsdestroyers.systems.ingame.logic;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.TimeUtils;
import com.google.common.eventbus.Subscribe;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.ActionState;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.components.TilePosition;
import org.salondesdevs.superdungeonsdestroyers.events.input.KeyPressedEvent;
import org.salondesdevs.superdungeonsdestroyers.library.components.Direction;
import org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Animation;
import org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerMotionSystem extends Service {

    private Int2ObjectMap<Direction> keysToDirection = new Int2ObjectArrayMap<>();

    @Inject
    NetworkSystem networkSystem;

    @Inject
    Mapper<TilePosition> positionMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    Mapper<ActionState> actionStateMapper;

    @Inject
    Animator animator;

    @Inject
    PlayerIdHolder playerIdHolder;

    private long lastMoved = 0L;

    private static final long delay = 300L;

    @Override
    public void initialize() {
        keysToDirection.put(Input.Keys.UP, Direction.NORTH);
        keysToDirection.put(Input.Keys.DOWN, Direction.SOUTH);
        keysToDirection.put(Input.Keys.RIGHT, Direction.EAST);
        keysToDirection.put(Input.Keys.LEFT, Direction.WEST);
    }

    @Subscribe
    public void keyPressed(KeyPressedEvent keyPressedEvent) {
        ActionState state = actionStateMapper.get(playerIdHolder.getEntityId());
        if (state == ActionState.MOVING) {
            return;
        }

        if (keysToDirection.keySet().contains(keyPressedEvent.getKeycode())) {
            long now = TimeUtils.millis();

            if (now - lastMoved < delay) {
                return;
            }
            lastMoved = now;

            actionStateMapper.set(playerIdHolder.getEntityId(), ActionState.MOVING);

            final Direction direction = keysToDirection.get(keyPressedEvent.getKeycode());

            TilePosition tilePosition = positionMapper.get(playerIdHolder.getEntityId());
            Offset offset = offsetMapper.get(playerIdHolder.getEntityId());

            switch (direction) {
                case NORTH:
                    tilePosition.y++;
                    break;
                case SOUTH:
                    tilePosition.y--;
                    break;
                case WEST:
                    tilePosition.x--;
                    break;
                case EAST:
                    tilePosition.x++;
                    break;
            }

//            networkSystem.request().addMoveContent(direction).writeAndFlush();

            Animation<Float> walkAnimation = animator.createMoveAnimation(offset, direction, () -> {
                actionStateMapper.set(playerIdHolder.getEntityId(), ActionState.IDLE);
            });

            animator.play(walkAnimation);
        }
    }
}
