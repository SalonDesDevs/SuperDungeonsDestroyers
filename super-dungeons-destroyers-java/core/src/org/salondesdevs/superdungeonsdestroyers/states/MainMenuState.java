package org.salondesdevs.superdungeonsdestroyers.states;

import javax.inject.Inject;

import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.I18NService;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.MainMenuScreen;

import net.wytrem.ecs.World;

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
        register(I18NService.class);
        register(NetworkSystem.class);
        register(NetworkHandlerSystem.class);
    }

    @Override
    public void pushed() {
        uiSystem.displayScreen(MainMenuScreen.class);
    }
}
