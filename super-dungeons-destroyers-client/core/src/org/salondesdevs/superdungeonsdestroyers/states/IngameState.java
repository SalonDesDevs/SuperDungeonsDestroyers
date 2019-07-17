package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Position;
import org.salondesdevs.superdungeonsdestroyers.components.Sprited;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.CameraService;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.CameraSyncSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.EntityCreator;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.GroundRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.IngameNetHandler;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.InputSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.MapSwitcher;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.OverlayRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.SpriteRenderer;

import javax.inject.Inject;

public class IngameState extends GameState {

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    Assets assetService;

    @Inject
    World world;
    @Inject
    NetworkHandlerSystem networkHandlerSystem;


    public IngameState() {
        super();

        // Be aware, the order matters.
        register(Assets.class);
        register(NetworkSystem.class);
        register(NetworkHandlerSystem.class);
        register(MapSwitcher.class);
        register(CameraService.class);
        register(CameraSyncSystem.class);
        register(ClearScrenSystem.class);
        register(GroundRenderSystem.class);
        register(SpriteRenderer.class);
        register(OverlayRenderSystem.class);
        register(InputSystem.class);
    }

    @Inject
    EntityCreator entityCreator;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Mapper<Camera> cameraMapper;

    @Override
    public void pushed() {
        this.networkHandlerSystem.setCurrentHandler(IngameNetHandler.class);

        int terrain = -1;
        terrainMapper.set(terrain, new Terrain(assetService.testMap));

        int playerTest = world.createEntity();
        entityCreator.addSprited(playerTest, Sprited.Sprites.MAGE);

        Position pos = new Position();
        pos.x = pos.y = 1;

        positionMapper.set(playerTest, pos);


        cameraMapper.set(playerTest, Camera.INSTANCE);

    }
}
