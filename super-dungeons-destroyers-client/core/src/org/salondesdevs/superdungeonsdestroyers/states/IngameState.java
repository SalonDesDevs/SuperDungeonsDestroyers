package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.systems.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.CameraService;
import org.salondesdevs.superdungeonsdestroyers.systems.GroundRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.InputSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.OverlayRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.RenderingSystem;

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
