package org.salondesdevs.superdungeonsdestroyers.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import org.lwjgl.opengl.GL11;

public class BackgroundColor extends BaseDrawable implements TransformDrawable {

    private ShapeRenderer shapeRenderer;
    private Color color;

    public BackgroundColor() {
        this(Color.WHITE);
    }

    public BackgroundColor(Color color) {
        this.shapeRenderer = new ShapeRenderer();
        this.color = new Color().set(color);
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.end();
        Gdx.gl.glEnable(GL11.GL_BLEND);
        Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(this.color);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL11.GL_BLEND);
        batch.begin();
    }

    @Override
    public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        batch.end();
        Gdx.gl.glEnable(GL11.GL_BLEND);
        Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(this.color);
        shapeRenderer.rect(x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL11.GL_BLEND);
        batch.begin();

    }
}
