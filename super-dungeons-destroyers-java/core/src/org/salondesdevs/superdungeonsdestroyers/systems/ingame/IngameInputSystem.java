package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.wytrem.ecs.BaseSystem;
import org.salondesdevs.superdungeonsdestroyers.events.KeyPressedEvent;
import org.salondesdevs.superdungeonsdestroyers.events.KeyReleasedEvent;
import org.salondesdevs.superdungeonsdestroyers.events.MouseScrolledEvent;
import org.salondesdevs.superdungeonsdestroyers.library.events.Event;
import org.salondesdevs.superdungeonsdestroyers.library.events.core.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class IngameInputSystem extends BaseSystem implements InputProcessor {

    private IntList pressedKeys;

    @Inject
    EventBus eventBus;

    private List<Event> eventsToPost;

    @Override
    public void initialize() {
        eventsToPost = new ArrayList<>();
        pressedKeys = new IntArrayList();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void process() {
        // Necessary because the InputProcessor methods are called by libgdx itself, outside of the World::process, so
        // the systems order is not respected.
        eventsToPost.forEach(this.eventBus::post);
        eventsToPost.clear();

        if (this.repeatEvents) {
            this.pressedKeys.stream().map(key -> new KeyPressedEvent(key, true)).forEach(this.eventBus::post);
        }
    }

    public boolean isKeyDown(int keycode) {
        return this.pressedKeys.contains(keycode);
    }

    public boolean isAnyKeyDown(IntSet intSet) {
        IntIterator intIterator = intSet.iterator();

        while (intIterator.hasNext()) {
            if (isKeyDown(intIterator.nextInt())) {
                return true;
            }
        }

        return false;
    }

    private boolean repeatEvents = true;

    public void enableRepeatEvents(boolean repeatEvents) {
        this.repeatEvents = repeatEvents;
    }

    public boolean areRepeatEventsEnabled() {
        return repeatEvents;
    }

    @Override
    public boolean keyDown(int keycode) {
        pressedKeys.add(keycode);
        this.eventsToPost.add(new KeyPressedEvent(keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        this.eventsToPost.add(new KeyReleasedEvent(keycode));
        pressedKeys.rem(keycode);
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
        this.eventsToPost.add(new MouseScrolledEvent(amount));
        return false;
    }
}
