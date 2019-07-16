package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.SuperDungeonsDestroyers;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CameraService extends Service {

    public OrthographicCamera camera;
    public OrthographicCamera guiCamera;
    public final float zoom;

    @Inject
    SuperDungeonsDestroyers sdd;

    public CameraService() {
        this.zoom = 2.f;
    }


    @Override
    public void initialize() {
        resized();
        sdd.addResizeListener(this::resized);
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