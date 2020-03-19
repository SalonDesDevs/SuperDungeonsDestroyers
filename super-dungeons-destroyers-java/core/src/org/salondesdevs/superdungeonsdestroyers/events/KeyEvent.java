package org.salondesdevs.superdungeonsdestroyers.events;

import org.salondesdevs.superdungeonsdestroyers.library.events.Event;

public abstract class KeyEvent extends Event {

    private final int keycode;

    public KeyEvent(int keycode) {
        this.keycode = keycode;
    }

    public int getKeycode() {
        return keycode;
    }
}
