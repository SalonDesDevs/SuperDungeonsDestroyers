package org.salondesdevs.superdungeonsdestroyers.systems.ingame.logic;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Me;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Simply stores (and keeps up to date) the ID of the entity that carries {@link Me}.
 */
@Singleton
public class PlayerIdHolder extends IteratingSystem {
    private int entityId;

    public PlayerIdHolder() {
        super(Aspect.all(Me.class));
    }

    @Override
    public void process(int entity) {
        this.entityId = entity;
    }

    public int getEntityId() {
        return entityId;
    }
}
