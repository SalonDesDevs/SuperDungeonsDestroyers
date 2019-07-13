package org.salondesdevs.superdungeonsdestroyers.systems;

import com.badlogic.gdx.graphics.Texture;
import net.wytrem.ecs.*;
import net.wytrem.ecs.Service;
import net.wytrem.ecs.utils.*;

import javax.inject.Singleton;

@Singleton
public class AssetService extends Service {
    public Texture img;

    @Override
    public void initialize() {
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
