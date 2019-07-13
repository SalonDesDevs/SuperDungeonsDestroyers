package org.salondesdevs.superdungeonsdestroyers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.*;

public class SuperDungeonsDestroyers extends ApplicationAdapter {
	World world;

	@Override
	public void create () {
		WorldConfiguration worldConfiguration = new WorldConfiguration();
		worldConfiguration.register(RenderingSystem.class);

		world = new World(worldConfiguration);
		world.initialize();
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
