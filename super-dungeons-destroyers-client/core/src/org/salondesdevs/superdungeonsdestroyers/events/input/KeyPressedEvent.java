package org.salondesdevs.superdungeonsdestroyers.events.input;

public class KeyPressedEvent extends KeyEvent {

    private final boolean isRepeatedEvent;

    public KeyPressedEvent(int keycode) {
        this(keycode, false);
    }

    public KeyPressedEvent(int keycode, boolean isRepeatedEvent) {
        super(keycode);
        this.isRepeatedEvent = isRepeatedEvent;
    }

    public boolean isRepeatedEvent() {
        return isRepeatedEvent;
    }
}
