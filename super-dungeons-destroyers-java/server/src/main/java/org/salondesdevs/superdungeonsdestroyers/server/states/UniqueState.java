package org.salondesdevs.superdungeonsdestroyers.server.states;

import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;
import org.salondesdevs.superdungeonsdestroyers.server.systems.EntityCreator;
import org.salondesdevs.superdungeonsdestroyers.server.systems.EnvironmentManager;
import org.salondesdevs.superdungeonsdestroyers.server.systems.MapLoader;
import org.salondesdevs.superdungeonsdestroyers.server.systems.MotionSystem;
import org.salondesdevs.superdungeonsdestroyers.server.systems.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.server.systems.Synchronizer;

import net.wytrem.ecs.GameState;

public class UniqueState extends GameState {
    public UniqueState() {
        register(EventBus.class);
        register(NetworkSystem.class);
        register(MapLoader.class);
        register(MotionSystem.class);
//        register(NameRandomizer.class);
        register(Synchronizer.class);
        register(EntityCreator.class);
        register(EnvironmentManager.class);
    }
}
