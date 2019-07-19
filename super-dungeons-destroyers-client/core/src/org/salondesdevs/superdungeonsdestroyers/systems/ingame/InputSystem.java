package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntArray;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.events.input.KeyPressedEvent;
import org.salondesdevs.superdungeonsdestroyers.events.input.KeyReleasedEvent;
import org.salondesdevs.superdungeonsdestroyers.events.input.MouseScrolledEvent;
import org.salondesdevs.superdungeonsdestroyers.systems.common.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class InputSystem extends BaseSystem implements InputProcessor {

    private IntArray pressedKeys;

    @Inject
    EventBus eventBus;

    @Override
    public void initialize() {
        pressedKeys = new IntArray();
        Gdx.input.setInputProcessor(this);
    }


    @Override
    public void process() {
        // TODO: If repeated events are enabled, loop through the pressedKeys and repeat the pressed.
    }

    @Override
    public boolean keyDown(int keycode) {
        pressedKeys.add(keycode);
        this.eventBus.post(new KeyPressedEvent(keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        this.eventBus.post(new KeyReleasedEvent(keycode));
        pressedKeys.removeValue(keycode);
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
        eventBus.post(new MouseScrolledEvent(amount));
        return false;
    }
}
