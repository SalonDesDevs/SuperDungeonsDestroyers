package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver.ConnectingToServerNetHandler;

import javax.inject.Inject;

public class ConnectingToServer extends SDDState {

    @Inject
    NetworkHandlerSystem networkHandlerSystem;

    @Inject
    World world;

    @Inject
    NetworkSystem networkSystem;

    public ConnectingToServer() {
        super();
        register(ClearScrenSystem.class);
        register(UiSystem.class);
        register(NetworkSystem.class);
        register(NetworkHandlerSystem.class);
    }

    @Override
    public void pushed() {
        super.pushed();
        // At the moment, this state pushes the next one immediately.
//        this.world.push(IngameState.class);
        this.networkHandlerSystem.setCurrentHandler(ConnectingToServerNetHandler.class);
        networkSystem.tryConnect("localhost", 9000);
    }
}
