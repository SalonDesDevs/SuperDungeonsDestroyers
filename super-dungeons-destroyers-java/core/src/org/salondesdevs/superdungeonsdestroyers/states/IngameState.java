package org.salondesdevs.superdungeonsdestroyers.states;

import org.salondesdevs.superdungeonsdestroyers.content.AnimationsCreator;
import org.salondesdevs.superdungeonsdestroyers.library.SDDState;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.I18NService;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.*;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.logic.PlayerIdHolder;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.logic.PlayerMotionSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.render.*;

import javax.inject.Inject;

public class IngameState extends SDDState {
    @Inject
    NetworkHandlerSystem networkHandlerSystem;


    public IngameState() {
        super();

        // Be aware, the order matters.

        // Services
        register(Assets.class);
        register(I18NService.class);
        register(EntityCreator.class);
        register(AnimationsCreator.class);
        register(Synchroniser.class);
        register(ClientChat.class);

        // Network stuff
        register(NetworkSystem.class);
        register(NetworkHandlerSystem.class);

        // Input
        register(IngameInputSystem.class);
        register(PlayerMotionSystem.class);

        // Updating stuff
        register(LevelSwitcher.class);
        register(Animator.class);
        register(PlayerIdHolder.class);

        // Rendering stuff
        register(CameraSystem.class);
        register(ClearScrenSystem.class);
        register(GroundRenderer.class);
        register(SpriteRenderer.class);
        register(AnimatedSpriteRenderer.class);
        register(OverlayRenderer.class);
        register(NameRenderer.class);
        register(HudRenderer.class);
        register(UiSystem.class);
    }

    @Inject
    UiSystem uiSystem;

    @Override
    public void pushed() {
        super.pushed();

        uiSystem.displayScreen(null);
    }

    @Override
    public String toString() {
        return "Ingame";
    }
}
