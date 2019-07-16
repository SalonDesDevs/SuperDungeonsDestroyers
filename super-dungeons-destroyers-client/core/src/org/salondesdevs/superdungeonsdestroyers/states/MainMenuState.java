package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.MainMenuRenderer;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;

public class MainMenuState extends GameState {
    public MainMenuState() {
        super();
        register(ClearScrenSystem.class);
        register(MainMenuRenderer.class);
    }
}
