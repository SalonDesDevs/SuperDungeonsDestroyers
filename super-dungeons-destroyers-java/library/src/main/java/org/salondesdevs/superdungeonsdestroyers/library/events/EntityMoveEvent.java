package org.salondesdevs.superdungeonsdestroyers.library.events;

import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;

public class EntityMoveEvent extends EntityEvent {
    private final Facing direction;

    public EntityMoveEvent(int entityId, Facing direction) {
        super(entityId);
        this.direction = direction;
    }

    public Facing getDirection() {
        return direction;
    }
}
