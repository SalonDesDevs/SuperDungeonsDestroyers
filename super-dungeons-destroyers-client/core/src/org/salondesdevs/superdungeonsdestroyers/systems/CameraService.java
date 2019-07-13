package org.salondesdevs.superdungeonsdestroyers.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.wytrem.ecs.*;

import javax.inject.Singleton;

@Singleton
public class CameraService extends BaseSystem {

    public OrthographicCamera camera;
    public OrthographicCamera guiCamera;
    public final float zoom;

    public CameraService() {
        this.zoom = 1.0f;
        resized();
    }

    // TODO: to be called when the window is resized
    public void resized() {
        float zoomFactorInverter = 1f/zoom;
        setupViewport(Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
    }

    protected void setupViewport( float width, float height) {
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();

        guiCamera = new OrthographicCamera(width, height);
        guiCamera.setToOrtho(false, width, height);
        guiCamera.update();
    }
}