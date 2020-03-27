package org.salondesdevs.superdungeonsdestroyers.server.states;

import org.salondesdevs.superdungeonsdestroyers.library.SDDState;
import org.salondesdevs.superdungeonsdestroyers.server.systems.*;
import org.salondesdevs.superdungeonsdestroyers.server.systems.net.NetworkSystem;

import javax.inject.Singleton;

@Singleton
public class UniqueState extends SDDState {
    public UniqueState() {
        register(PlayerComponentsInitializer.class);
        register(ChatSystem.class);
        register(CollisionDetector.class);
        register(NetworkSystem.class);
        register(MapLoader.class);
        register(MotionSystem.class);
        register(Synchronizer.class);
        register(EntityCreatorServer.class);
        register(EnvironmentManager.class);
    }

    @Override
    public String toString() {
        return "UniqueServerState";
    }
}
