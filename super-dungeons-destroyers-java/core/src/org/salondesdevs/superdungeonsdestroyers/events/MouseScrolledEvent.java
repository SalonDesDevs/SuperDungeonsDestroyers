package org.salondesdevs.superdungeonsdestroyers.events;

import org.salondesdevs.superdungeonsdestroyers.library.events.Event;

public class MouseScrolledEvent extends Event {

    private final int amount;

    public MouseScrolledEvent(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
