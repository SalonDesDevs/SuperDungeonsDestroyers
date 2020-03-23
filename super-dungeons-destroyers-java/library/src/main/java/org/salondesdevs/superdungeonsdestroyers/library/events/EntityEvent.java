package org.salondesdevs.superdungeonsdestroyers.library.events;

public class EntityEvent extends Event {
    private final int entityId;

    public EntityEvent(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }
}
