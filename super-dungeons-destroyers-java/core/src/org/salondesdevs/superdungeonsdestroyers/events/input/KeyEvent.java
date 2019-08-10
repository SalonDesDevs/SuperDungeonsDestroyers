package org.salondesdevs.superdungeonsdestroyers.events.input;

import org.salondesdevs.superdungeonsdestroyers.events.Event;

public abstract class KeyEvent extends Event {

    private final int keycode;

    public KeyEvent(int keycode) {
        this.keycode = keycode;
    }

    public int getKeycode() {
        return keycode;
    }
}
