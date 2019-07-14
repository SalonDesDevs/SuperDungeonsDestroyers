package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;

public class ConnectingToServer extends GameState {
    public ConnectingToServer() {
        super();
        register(ClearScrenSystem.class);
    }
}
