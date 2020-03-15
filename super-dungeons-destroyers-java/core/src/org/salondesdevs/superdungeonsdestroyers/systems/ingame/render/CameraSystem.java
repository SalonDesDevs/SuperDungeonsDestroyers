package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.salondesdevs.superdungeonsdestroyers.SuperDungeonsDestroyersClient;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;

@Singleton
public class CameraSystem extends IteratingSystem {
    public OrthographicCamera camera;
    public OrthographicCamera guiCamera;
    public final float zoom;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    SuperDungeonsDestroyersClient sdd;

    public CameraSystem() {
        super(Aspect.all(Position.class, Camera.class));
        this.zoom = 1.f;
    }


    @Override
    public void initialize() {
        resized();
        sdd.addResizeListener(this::resized);
    }

    @Override
    public void process(int entity) {
        Position position = positionMapper.get(entity);

        if (offsetMapper.has(entity)) {
            Offset offset = offsetMapper.get(entity);

            camera.position.x = GridSpriteBatch.toGridCoordsX(position, offset);
            camera.position.y = GridSpriteBatch.toGridCoordsY(position, offset);
        }
        else {
            camera.position.x = GridSpriteBatch.toGridCoordsX(position);
            camera.position.y = GridSpriteBatch.toGridCoordsY(position);
        }
        camera.update();
    }

    public void resized() {
        float zoomFactorInverter = 1f / zoom;
        setupViewport(Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
    }

    private void setupViewport(float width, float height) {
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();

        guiCamera = new OrthographicCamera(width, height);
        guiCamera.setToOrtho(false, width, height);
        guiCamera.update();
    }
}