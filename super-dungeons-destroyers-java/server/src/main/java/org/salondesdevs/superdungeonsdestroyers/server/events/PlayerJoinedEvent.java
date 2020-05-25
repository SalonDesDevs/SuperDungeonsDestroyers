package org.salondesdevs.superdungeonsdestroyers.server.events;

import org.salondesdevs.superdungeonsdestroyers.library.events.PlayerEvent;

/**
 * Posted when a player entity is created (ie when a client joins the server).
 */
public class PlayerJoinedEvent extends PlayerEvent {
    public PlayerJoinedEvent(int player) {
        super(player);
    }
}
