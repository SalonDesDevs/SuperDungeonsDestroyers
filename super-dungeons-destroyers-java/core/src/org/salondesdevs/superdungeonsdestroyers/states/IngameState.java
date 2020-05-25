package org.salondesdevs.superdungeonsdestroyers.states;

import org.salondesdevs.superdungeonsdestroyers.content.AnimationsCreator;
import org.salondesdevs.superdungeonsdestroyers.library.SDDState;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.content.Assets;
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
import javax.inject.Singleton;

@Singleton
public class IngameState extends SDDState {
    @Inject
    NetworkHandlerSystem networkHandlerSystem;


    public IngameState() {
        super();

        // Be aware, the order matters.

        // Services
        // -- for these, the order doesn't matter
        register(Assets.class);
        register(I18NService.class);
        register(EntityCreatorClient.class);
        register(AnimationsCreator.class);
        register(SynchroniserClient.class);
        register(ClientChat.class);
        register(AutomaticAnimationSwitcher.class);

        // Networking stuff
        register(NetworkSystem.class);
        register(NetworkHandlerSystem.class);

        // Input stuff
        register(PlayerMotionSystem.class);
        register(IngameInputSystem.class);

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
