package org.salondesdevs.superdungeonsdestroyers.library.events;

public class EntityEvent extends Event {
    private final int entity;

    public EntityEvent(int entity) {
        this.entity = entity;
    }

    public int getEntity() {
        return entity;
    }
}
