package org.salondesdevs.superdungeonsdestroyers.library.events;

public class PlayerEvent extends Event {
    private final int player;

    public PlayerEvent(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }
}
