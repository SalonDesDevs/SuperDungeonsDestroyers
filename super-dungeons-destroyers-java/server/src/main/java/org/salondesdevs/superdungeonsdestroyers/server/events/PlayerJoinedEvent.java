package org.salondesdevs.superdungeonsdestroyers.server.events;

import org.salondesdevs.superdungeonsdestroyers.library.events.PlayerEvent;

public class PlayerJoinedEvent extends PlayerEvent {
    public PlayerJoinedEvent(int player) {
        super(player);
    }
}
