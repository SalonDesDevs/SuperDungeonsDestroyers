package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;

import javax.inject.Inject;

public class SDDState extends GameState {

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
