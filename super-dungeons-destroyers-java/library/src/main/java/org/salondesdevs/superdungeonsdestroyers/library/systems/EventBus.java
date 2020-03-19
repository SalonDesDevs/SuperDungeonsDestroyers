package org.salondesdevs.superdungeonsdestroyers.library.systems;

import net.wytrem.ecs.BaseSystem;
import net.wytrem.ecs.GameState;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class EventBus extends Service {

    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    private com.google.common.eventbus.EventBus globalEventBus;

    @Override
    public void initialize() {
        globalEventBus = new com.google.common.eventbus.EventBus();
    }

    public void register(Object object) {
//        logger.trace("register(" + "object = " + object + ")");

        globalEventBus.register(object);
    }

    public void unregister(Object object) {
//        logger.trace("unregister(" + "object = " + object + ")");

        globalEventBus.unregister(object);
    }

    public void registerGameState(GameState gameState) {
        for (BaseSystem sys : gameState.systems().allSystems()) {
            register(sys);
        }
    }

    public void unregisterGameState(GameState gameState) {
        for (BaseSystem sys : gameState.systems().allSystems()) {
            unregister(sys);
        }
    }

    public void post(Event event) {
        globalEventBus.post(event);
    }
}
