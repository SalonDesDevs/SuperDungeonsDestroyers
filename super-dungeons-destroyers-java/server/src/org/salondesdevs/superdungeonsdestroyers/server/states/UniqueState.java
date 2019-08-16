package org.salondesdevs.superdungeonsdestroyers.server.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;
import org.salondesdevs.superdungeonsdestroyers.server.systems.EntityCreator;
import org.salondesdevs.superdungeonsdestroyers.server.systems.EnvironmentManager;
import org.salondesdevs.superdungeonsdestroyers.server.systems.Synchronizer;
import org.salondesdevs.superdungeonsdestroyers.server.systems.MotionSystem;
import org.salondesdevs.superdungeonsdestroyers.server.systems.NetworkSystem;

public class UniqueState extends GameState {
    public UniqueState() {
        register(EventBus.class);
        register(NetworkSystem.class);
        register(MotionSystem.class);
        register(Synchronizer.class);
        register(EntityCreator.class);
        register(EnvironmentManager.class);
    }
}
