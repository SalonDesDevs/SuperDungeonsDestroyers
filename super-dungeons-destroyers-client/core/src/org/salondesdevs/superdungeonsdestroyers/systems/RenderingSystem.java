package org.salondesdevs.superdungeonsdestroyers.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.*;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RenderingSystem extends BaseSystem {
    @Inject
    AssetService assetService;


    SpriteBatch batch;

    @Override
    public void initialize() {
        batch = new SpriteBatch();
    }

    @Override
    public void begin() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
    }

    @Override
    public void process() {
        //batch.draw(assetService.img, 0, 0);
    }

    @Override
    public void end() {
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
