package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import SDD.Common.Direction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.TimeUtils;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.components.TilePosition;
import org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Animation;
import org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.systems.common.animations.Interpolators;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;

@Singleton
public class InputSystem extends IteratingSystem implements InputProcessor {

    IntArray keys;

    @Inject
    CameraSystem cameraService;

    @Inject
    NetworkSystem networkSystem;

    public InputSystem() {
        super(Aspect.all(TilePosition.class, Offset.class));
    }

    @Override
    public void initialize() {
        keys = new IntArray();
        Gdx.input.setInputProcessor(this);
    }

    @Inject
    Mapper<TilePosition> positionMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    Animator animator;

    long lastMoved = 0l, now;

    private static final long delay = 300l;

    @Override
    //TODO: this is temporary
    public void process(int entity) {


        now = TimeUtils.millis();

        if (now - lastMoved > delay) {

            byte direction = -1;

            boolean pressedMoveKey = false;


            if (keys.contains(Input.Keys.UP)) {
                direction = Direction.Up;
                pressedMoveKey = true;
            }
            if (keys.contains(Input.Keys.DOWN)) {
                direction = Direction.Down;
                pressedMoveKey = true;
            }
            if (keys.contains(Input.Keys.LEFT)) {
                direction = Direction.Left;
                pressedMoveKey = true;
            }
            if (keys.contains(Input.Keys.RIGHT)) {
                direction = Direction.Right;
                pressedMoveKey = true;
            }

            if (pressedMoveKey) {
                TilePosition tilePosition = positionMapper.get(entity);
                Offset offset = offsetMapper.get(entity);
                lastMoved = now;

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

    @Override
    public boolean keyDown(int keycode) {
        keys.add(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.removeValue(keycode);
        lastMoved = 0;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
