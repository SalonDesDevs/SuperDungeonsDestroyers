package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.RenderingSystem;

public class MainMenuState extends GameState {
    public MainMenuState() {
        super();
        register(RenderingSystem.class);
    }
}
