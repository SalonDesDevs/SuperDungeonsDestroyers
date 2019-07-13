package org.salondesdevs.superdungeonsdestroyers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.AssetService;
import org.salondesdevs.superdungeonsdestroyers.systems.CameraService;
import org.salondesdevs.superdungeonsdestroyers.systems.DebugSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.GroundRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.InputSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.OverlayRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.RenderingSystem;

public class SuperDungeonsDestroyers extends ApplicationAdapter {
	World world;

	@Override
	public void create () {
		WorldConfiguration worldConfiguration = new WorldConfiguration();

		// Be aware, the order matters.
		worldConfiguration.register(AssetService.class);
		worldConfiguration.register(CameraService.class);
		worldConfiguration.register(RenderingSystem.class);
		worldConfiguration.register(GroundRenderSystem.class);
		worldConfiguration.register(OverlayRenderSystem.class);
		worldConfiguration.register(InputSystem.class);
		worldConfiguration.register(DebugSystem.class);

		world = new World(worldConfiguration);
		world.initialize();
	}

	@Override
	public void resize(int width, int height) {
		// TODO: dispatch event through systems (see CameraSystem::resized)

		// TODO: ugly
		CameraService.instance.resized();
	}

	@Override
	public void render () {
		world.process(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		world.dispose();
	}
}
