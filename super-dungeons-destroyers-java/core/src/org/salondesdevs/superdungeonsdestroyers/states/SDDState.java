package org.salondesdevs.superdungeonsdestroyers.states;

import javax.inject.Inject;

import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;

import net.wytrem.ecs.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SDDState extends GameState {

    private static final Logger logger = LoggerFactory.getLogger(SDDState.class);

    @Inject
    EventBus eventBus;

    public SDDState() {
        super();
        register(EventBus.class);
    }

    @Override
    public void pushed() {
        logger.info("Pushed game state {}", this.toString());

        eventBus.registerGameState(this);
    }

    @Override
    public void poped() {
        eventBus.unregisterGameState(this);
    }
}
