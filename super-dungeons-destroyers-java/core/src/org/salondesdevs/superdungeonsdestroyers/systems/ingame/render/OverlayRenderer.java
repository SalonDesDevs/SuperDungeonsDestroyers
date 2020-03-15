package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import static com.badlogic.gdx.graphics.GL20.GL_KEEP;
import static com.badlogic.gdx.graphics.GL20.GL_REPLACE;
import static com.badlogic.gdx.graphics.GL20.GL_STENCIL_TEST;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.components.Terrain;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Size;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.CrossIteratingSystem;
import net.wytrem.ecs.Mapper;

/**
 * If a {@link org.salondesdevs.superdungeonsdestroyers.components.Terrain} entity is present, will use it to render the
 * map layer called "overlay".
 */
@Singleton
public class OverlayRenderer extends CrossIteratingSystem {

    @Inject
    MapLayerRenderer mapLayerRenderer;

    public OverlayRenderer() {
        super(Aspect.all(Terrain.class), Aspect.all(Camera.class, Position.class, Offset.class, Size.class));
    }

    @Override
    public void initialize() {
        this.mapLayerRenderer.initialize("overlay");
        this.shapeRenderer = new ShapeRenderer();
    }

    @Inject
    Mapper<Terrain> terrainMapper;

    @Inject
    Mapper<Size> sizeMapper;

    @Inject
    CameraSystem cameraSystem;

    ShapeRenderer shapeRenderer;

    private static final float SIZE_FACTOR = 1.f;

    @Override
    public void process(int first, int second) {
        Terrain terrain = terrainMapper.get(first);
        Size size = sizeMapper.get(second);


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
        this.mapLayerRenderer.render(terrain);

        Gdx.gl.glDisable(GL_STENCIL_TEST);
    }
}
