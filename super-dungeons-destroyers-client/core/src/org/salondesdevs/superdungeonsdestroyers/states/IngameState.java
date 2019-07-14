package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.systems.RenderingSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.CameraService;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.GroundRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.InputSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.OverlayRenderSystem;

import javax.inject.Inject;

public class IngameState extends GameState {

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    Assets assetService;

    @Inject
    World world;


    public IngameState() {
        super();

        // Be aware, the order matters.
        register(Assets.class);
        register(CameraService.class);
        register(ClearScrenSystem.class);
        register(RenderingSystem.class);
        register(GroundRenderSystem.class);
        register(OverlayRenderSystem.class);
        register(InputSystem.class);
    }

    @Override
    public void pushed() {
        int terrain = world.createEntity();
        terrainMapper.set(terrain, new Terrain(assetService.testMap));
    }
}
