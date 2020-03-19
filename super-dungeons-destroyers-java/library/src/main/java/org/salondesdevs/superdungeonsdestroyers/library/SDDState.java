package org.salondesdevs.superdungeonsdestroyers.library;

import net.wytrem.ecs.GameState;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

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
        logger.info("Poped game state {}", this.toString());

        eventBus.unregisterGameState(this);
    }
}
