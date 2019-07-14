package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver.NetworkSystem;

public class ConnectingToServer extends GameState {
    public ConnectingToServer() {
        super();
        register(ClearScrenSystem.class);
        register(NetworkSystem.class);
    }
}
