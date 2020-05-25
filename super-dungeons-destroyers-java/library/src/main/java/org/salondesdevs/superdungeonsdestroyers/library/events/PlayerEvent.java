package org.salondesdevs.superdungeonsdestroyers.library.events;

public class PlayerEvent extends EntityEvent {
    public PlayerEvent(int player) {
        super(player);
    }

    public int getPlayer() {
        return this.getEntity();
    }
}
