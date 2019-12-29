package org.salondesdevs.superdungeonsdestroyers.server.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;
import org.salondesdevs.superdungeonsdestroyers.server.systems.*;

public class UniqueState extends GameState {
    public UniqueState() {
        register(EventBus.class);
        register(NetworkSystem.class);
        register(MotionSystem.class);
//        register(NameRandomizer.class);
        register(Synchronizer.class);
        register(EntityCreator.class);
        register(EnvironmentManager.class);
    }
}
