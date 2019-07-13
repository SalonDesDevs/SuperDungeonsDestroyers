package org.salondesdevs.superdungeonsdestroyers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.*;

public class RenderingSystem extends BaseSystem {

    SpriteBatch batch;
    Texture img;

    @Override
    public void initialize() {
        batch = new SpriteBatch();

        img = new Texture("badlogic.jpg");

    }

    @Override
    public void process() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        img.dispose();
        batch.dispose();
    }
}
