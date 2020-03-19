package org.salondesdevs.superdungeonsdestroyers.events;

import org.salondesdevs.superdungeonsdestroyers.library.events.Event;

public class ConnectFailedEvent extends Event {
    private final Throwable cause;

    public ConnectFailedEvent(Throwable cause) {
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
