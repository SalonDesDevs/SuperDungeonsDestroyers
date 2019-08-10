package org.salondesdevs.superdungeonsdestroyers.server.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.server.systems.NetworkSystem;

public class UniqueState extends GameState {
    public UniqueState() {
        register(NetworkSystem.class);
    }
}
