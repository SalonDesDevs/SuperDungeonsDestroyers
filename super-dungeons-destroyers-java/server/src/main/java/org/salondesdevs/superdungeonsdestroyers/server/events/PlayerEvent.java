package org.salondesdevs.superdungeonsdestroyers.server.events;

import org.salondesdevs.superdungeonsdestroyers.library.events.Event;

public class PlayerEvent extends Event {
    private final int player;

    public PlayerEvent(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }
}
