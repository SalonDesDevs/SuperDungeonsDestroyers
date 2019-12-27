package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
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

    @Inject
    NetworkHandlerSystem networkHandlerSystem;

    @Inject
    NetworkSystem networkSystem;

    public MainMenuState() {
        super();
        // Services
        register(Assets.class);

        // Systems
        register(ClearScrenSystem.class);
        register(UiSystem.class);
        register(NetworkSystem.class);
        register(NetworkHandlerSystem.class);
    }

    @Override
    public void pushed() {
        uiSystem.displayScreen(MainMenuScreen.class);
    }
}
