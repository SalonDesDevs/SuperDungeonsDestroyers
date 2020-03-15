package org.salondesdevs.superdungeonsdestroyers.systems.ingame.logic;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.salondesdevs.superdungeonsdestroyers.components.ActionState;
import org.salondesdevs.superdungeonsdestroyers.content.AnimationsCreator;
import org.salondesdevs.superdungeonsdestroyers.events.input.KeyPressedEvent;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animation;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;

import com.badlogic.gdx.Input;
import com.google.common.eventbus.Subscribe;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;

@Singleton
public class PlayerMotionSystem extends Service {

    private Int2ObjectMap<Facing> keysToDirection = new Int2ObjectArrayMap<>();

    @Inject
    NetworkSystem networkSystem;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Mapper<ActionState> actionStateMapper;

    @Inject
    AnimationsCreator animationsCreator;

    @Inject
    Animator animator;

    @Inject
    PlayerIdHolder playerIdHolder;

    private long lastMoved = 0L;

    @Override
    public void initialize() {
        keysToDirection.put(Input.Keys.UP, Facing.NORTH);
        keysToDirection.put(Input.Keys.DOWN, Facing.SOUTH);
        keysToDirection.put(Input.Keys.RIGHT, Facing.EAST);
        keysToDirection.put(Input.Keys.LEFT, Facing.WEST);
    }

    @Subscribe
    public void keyPressed(KeyPressedEvent keyPressedEvent) {
        ActionState state = actionStateMapper.get(playerIdHolder.getEntityId());
        if (state == ActionState.MOVING) {
            return;
        }

        if (keysToDirection.keySet().contains(keyPressedEvent.getKeycode())) {

            actionStateMapper.set(playerIdHolder.getEntityId(), ActionState.MOVING);

            final Facing facing = keysToDirection.get(keyPressedEvent.getKeycode());

            Position position = positionMapper.get(playerIdHolder.getEntityId());

            switch (facing) {
                case NORTH:
                    position.y++;
                    break;
                case SOUTH:
                    position.y--;
                    break;
                case WEST:
                    position.x--;
                    break;
                case EAST:
                    position.x++;
                    break;
            }

            networkSystem.send(new PlayerMove(facing));

            Animation<Float> walkAnimation = animationsCreator.createMoveAnimationBasedOnSpeed(playerIdHolder.getEntityId(), facing, () -> {
                actionStateMapper.set(playerIdHolder.getEntityId(), ActionState.IDLE);
            });

            animator.play(walkAnimation);
        }
    }
}
