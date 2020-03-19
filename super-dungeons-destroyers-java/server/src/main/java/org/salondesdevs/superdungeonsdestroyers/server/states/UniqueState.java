package org.salondesdevs.superdungeonsdestroyers.server.states;

import org.salondesdevs.superdungeonsdestroyers.library.SDDState;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;
import org.salondesdevs.superdungeonsdestroyers.server.systems.*;
import org.salondesdevs.superdungeonsdestroyers.server.systems.net.NetworkSystem;

public class UniqueState extends SDDState {
    public UniqueState() {
        register(EventBus.class);
        register(PlayerComponentsInitializer.class);
        register(NetworkSystem.class);
        register(MapLoader.class);
        register(MotionSystem.class);
        register(Synchronizer.class);
        register(EntityCreator.class);
        register(EnvironmentManager.class);
    }

    @Override
    public String toString() {
        return "UniqueServerState";
    }
}
