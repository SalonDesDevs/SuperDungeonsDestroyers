package org.salondesdevs.superdungeonsdestroyers.systems.ingame.logic;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import org.salondesdevs.superdungeonsdestroyers.library.components.RemainingSteps;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.components.ActionState;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.content.AnimationsCreator;
import org.salondesdevs.superdungeonsdestroyers.events.KeyPressedEvent;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animation;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.IngameInputSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.LevelSwitcher;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerMotionSystem extends Service {

    private Int2ObjectMap<Facing> keysToDirection = new Int2ObjectArrayMap<>();

    @Inject
    IngameInputSystem ingameInputSystem;

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

    @Inject
    LevelSwitcher levelSwitcher;

    @Inject
    Mapper<Animated> animatedMapper;

    @Inject
    Mapper<RemainingSteps> remainingStepsMapper;

    @Override
    public void initialize() {
        keysToDirection.put(Input.Keys.UP, Facing.NORTH);
        keysToDirection.put(Input.Keys.DOWN, Facing.SOUTH);
        keysToDirection.put(Input.Keys.RIGHT, Facing.EAST);
        keysToDirection.put(Input.Keys.LEFT, Facing.WEST);
    }

    @EventHandler
    public void keyPressed(KeyPressedEvent keyPressedEvent) {
        ActionState state = actionStateMapper.get(playerIdHolder.getEntityId());
        if (state == ActionState.MOVING) {
            return;
        }

        if (keysToDirection.keySet().contains(keyPressedEvent.getKeycode())) {
            final Facing facing = keysToDirection.get(keyPressedEvent.getKeycode());

            Position position = positionMapper.get(playerIdHolder.getEntityId());

            // Check if tile is solid
            {
                int wantedX = position.x + facing.x;
                int wantedY = position.y + facing.y;

                TiledMapTile tile = this.levelSwitcher.getGround().getCell(wantedX, wantedY).getTile();
                Object solidProperty = tile.getProperties().get("solid");

                if (solidProperty != null) {
                    if (solidProperty instanceof String) {
                        if (((String) solidProperty).equals("true")) {
                            return;
                        }
                    } else if (solidProperty instanceof Boolean) {
                        if ((Boolean) solidProperty) {
                            return;
                        }
                    }
                }
            }
            // If it is not solid, process

            actionStateMapper.set(playerIdHolder.getEntityId(), ActionState.MOVING);
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
