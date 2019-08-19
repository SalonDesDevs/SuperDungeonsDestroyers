package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.MainMenuScreen;

import javax.inject.Inject;

public class MainMenuState extends SDDState {

    @Inject
    UiSystem uiSystem;

    @Inject
    Assets assets;

    @Inject
    World world;

    public MainMenuState() {
        super();
        // Services
        register(Assets.class);

        // Systems
        register(ClearScrenSystem.class);
        register(UiSystem.class);
    }

    @Override
    public void pushed() {
        uiSystem.displayScreen(MainMenuScreen.class);
    }
}
