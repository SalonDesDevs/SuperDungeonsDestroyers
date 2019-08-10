package org.salondesdevs.superdungeonsdestroyers.events.input;

import org.salondesdevs.superdungeonsdestroyers.events.Event;

public class MouseScrolledEvent extends Event {

    private final int amount;

    public MouseScrolledEvent(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
