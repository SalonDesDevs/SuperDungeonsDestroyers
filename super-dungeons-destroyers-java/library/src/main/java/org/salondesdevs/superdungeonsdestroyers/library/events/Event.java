package org.salondesdevs.superdungeonsdestroyers.library.events;

public abstract class Event {

    private boolean consumed;

    public boolean isConsumed() {
        return consumed;
    }

    public Event consume() {
        setConsumed(true);
        return this;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }
}
