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

    private static final long delay = 500l;

    @Override
    //TODO: this is temporary
    public void process(int entity) {
        TilePosition tilePosition = positionMapper.get(entity);
        Offset offset = offsetMapper.get(entity);

        now = TimeUtils.millis();

        if (now - lastMoved > delay) {
            Runnable onEnd = null;
            float start = 0;
            Consumer<Float> setter = null;

            if (keys.contains(Input.Keys.UP)) {
                start = -16.0f;
                tilePosition.y++;
                setter = offset::setY;
                onEnd = () -> networkSystem.request().addMoveContent(Direction.Up).writeAndFlush();
            }
            if (keys.contains(Input.Keys.DOWN)) {
                start = 16.0f;
                tilePosition.y--;
                setter = offset::setY;
                onEnd = () -> networkSystem.request().addMoveContent(Direction.Down).writeAndFlush();
            }
            if (keys.contains(Input.Keys.LEFT)) {
                start = 16.0f;
                tilePosition.x--;
                setter = offset::setX;
                onEnd = () -> networkSystem.request().addMoveContent(Direction.Left).writeAndFlush();
            }
            if (keys.contains(Input.Keys.RIGHT)) {
                start = -16.0f;
                tilePosition.x++;
                setter = offset::setX;
                onEnd = () -> networkSystem.request().addMoveContent(Direction.Right).writeAndFlush();
            }

            if (onEnd != null && now - lastMoved > delay) {
                lastMoved = now;

                Animation<Float> animation = new Animation<>(0.25f, start, 0.0f, Interpolators.LINEAR_FLOAT, setter, onEnd);
                animator.submit(animation);
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
