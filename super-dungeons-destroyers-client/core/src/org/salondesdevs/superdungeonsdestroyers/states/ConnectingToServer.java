package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver.ConnectingToServerNetHandler;

import javax.inject.Inject;

public class ConnectingToServer extends GameState {

    @Inject
    NetworkHandlerSystem networkHandlerSystem;

    public ConnectingToServer() {
        super();
        register(ClearScrenSystem.class);
        register(NetworkSystem.class);
        register(NetworkHandlerSystem.class);
    }

    @Override
    public void pushed() {

        this.networkHandlerSystem.setCurrentHandler(ConnectingToServerNetHandler.class);
    }
}
