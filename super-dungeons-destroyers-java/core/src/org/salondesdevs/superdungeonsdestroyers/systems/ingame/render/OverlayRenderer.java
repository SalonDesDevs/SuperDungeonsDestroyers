package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import static com.badlogic.gdx.graphics.GL20.GL_KEEP;
import static com.badlogic.gdx.graphics.GL20.GL_REPLACE;
import static com.badlogic.gdx.graphics.GL20.GL_STENCIL_TEST;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.wytrem.ecs.IteratingSystem;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Size;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.CrossIteratingSystem;
import net.wytrem.ecs.Mapper;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.LevelSwitcher;

@Singleton
public class OverlayRenderer extends IteratingSystem {

    @Inject
    MapLayerRenderer mapLayerRenderer;

    @Inject
    LevelSwitcher levelSwitcher;

    public OverlayRenderer() {
        super(Aspect.all(Camera.class, Position.class, Offset.class, Size.class));
    }

    @Override
    public void initialize() {
        this.mapLayerRenderer.initialize("overlay");
        this.shapeRenderer = new ShapeRenderer();
    }

    @Inject
    Mapper<Size> sizeMapper;

    @Inject
    CameraSystem cameraSystem;

    ShapeRenderer shapeRenderer;

    private static final float SIZE_FACTOR = 1.f;

    @Override
    public void process(int entity) {
        Size size = sizeMapper.get(entity);


        Gdx.gl.glEnable(GL_STENCIL_TEST);
        Gdx.gl.glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

        Gdx.gl.glColorMask(false, false, false, false);
        Gdx.gl.glDepthMask(false);
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xff);
        Gdx.gl.glStencilMask(0xff);
        shapeRenderer.setProjectionMatrix(cameraSystem.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.rect(cameraSystem.camera.position.x, cameraSystem.camera.position.y, size.getWidth() * SIZE_FACTOR, size.getHeight() * SIZE_FACTOR);
        shapeRenderer.end();


        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glStencilFunc(GL20.GL_NOTEQUAL, 1, 0xff);
        Gdx.gl.glStencilMask(0);
        this.mapLayerRenderer.render(levelSwitcher.getTerrain());

        Gdx.gl.glDisable(GL_STENCIL_TEST);
    }

    @Override
    public boolean isEnabled() {
        return this.levelSwitcher.getTerrain() != null;
    }
}
