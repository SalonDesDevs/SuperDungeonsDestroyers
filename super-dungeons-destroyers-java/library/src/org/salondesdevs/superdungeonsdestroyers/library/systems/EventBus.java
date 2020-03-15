package org.salondesdevs.superdungeonsdestroyers.library.systems;

import javax.inject.Singleton;

import org.salondesdevs.superdungeonsdestroyers.library.events.Event;

import net.wytrem.ecs.BaseSystem;
import net.wytrem.ecs.GameState;
import net.wytrem.ecs.Service;

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
