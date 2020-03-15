package org.salondesdevs.superdungeonsdestroyers.systems.ingame.logic;

import javax.inject.Singleton;

import org.salondesdevs.superdungeonsdestroyers.components.Me;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;

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
