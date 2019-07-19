package org.salondesdevs.superdungeonsdestroyers.systems.common;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.events.Event;

import javax.inject.Singleton;

@Singleton
public class EventBus extends Service {

    private com.google.common.eventbus.EventBus globalEventBus;

    @Override
    public void initialize() {
        globalEventBus = new com.google.common.eventbus.EventBus();
    }

    public void registerListeners(GameState gameState) {
        for (BaseSystem sys : gameState.systems().allSystems()) {
            globalEventBus.register(sys);
        }
    }

    public void unregisterListeners(GameState gameState) {
        for (BaseSystem sys : gameState.systems().allSystems()) {
            globalEventBus.unregister(sys);
        }
    }

    public void post(Event event) {
        globalEventBus.post(event);
    }
}
