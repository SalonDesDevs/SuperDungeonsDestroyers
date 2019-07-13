package org.salondesdevs.superdungeonsdestroyers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.google.inject.AbstractModule;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.states.TestState;
import org.salondesdevs.superdungeonsdestroyers.systems.AssetService;
import org.salondesdevs.superdungeonsdestroyers.systems.CameraService;
import org.salondesdevs.superdungeonsdestroyers.systems.DebugSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.GroundRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.InputSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.OverlayRenderSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.RenderingSystem;

import java.util.HashSet;
import java.util.Set;

public class SuperDungeonsDestroyers extends ApplicationAdapter {
	World world;

	private Set<Runnable> resizeListeners = new HashSet<Runnable>();

	@Override
	public void create () {
		WorldConfiguration worldConfiguration = new WorldConfiguration();
		worldConfiguration.getExtraModules().add(new AbstractModule() {
			@Override
			protected void configure() {
				bind(SuperDungeonsDestroyers.class).toInstance(SuperDungeonsDestroyers.this);
			}
		});

		world = new World(worldConfiguration);
		world.initialize();

		world.push(TestState.class);
	}

	public void addResizeListener(Runnable run) {
		this.resizeListeners.add(run);
	}

	@Override
	public void resize(int width, int height) {
		this.resizeListeners.forEach(Runnable::run);
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
