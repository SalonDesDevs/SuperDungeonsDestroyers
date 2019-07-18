package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.SuperDungeonsDestroyersClient;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Position;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CameraSystem extends IteratingSystem {
    public OrthographicCamera camera;
    public OrthographicCamera guiCamera;
    public final float zoom;


    @Inject
    Mapper<Position> positionMapper;

    @Inject
    SuperDungeonsDestroyersClient sdd;

    public CameraSystem() {
        super(Aspect.all(Position.class, Camera.class));
        this.zoom = 2.f;
    }


    @Override
    public void initialize() {
        resized();
        sdd.addResizeListener(this::resized);
    }

    @Override
    public void process(int entity) {
        Position position = positionMapper.get(entity);
        camera.position.x = position.x * 16.f;
        camera.position.y = position.y * 16.f;
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