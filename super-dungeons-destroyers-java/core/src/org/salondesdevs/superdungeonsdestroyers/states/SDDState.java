package org.salondesdevs.superdungeonsdestroyers.states;

import javax.inject.Inject;

import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;

import net.wytrem.ecs.GameState;

public abstract class SDDState extends GameState {

    @Inject
    EventBus eventBus;

    public SDDState() {
        super();
        register(EventBus.class);
    }

    @Override
    public void pushed() {
        eventBus.registerListeners(this);
    }

    @Override
    public void poped() {
        eventBus.unregisterListeners(this);
    }
}
