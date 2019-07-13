package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.AssetService;
import org.salondesdevs.superdungeonsdestroyers.systems.CameraService;
import org.salondesdevs.superdungeonsdestroyers.systems.DebugSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.GroundRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.InputSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.OverlayRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.RenderingSystem;

public class TestState extends GameState {
    public TestState() {
        super();

        // Be aware, the order matters.
        register(AssetService.class);
        register(CameraService.class);
        register(RenderingSystem.class);
        register(GroundRenderSystem.class);
        register(OverlayRenderSystem.class);
        register(InputSystem.class);
        register(DebugSystem.class);
    }
}
