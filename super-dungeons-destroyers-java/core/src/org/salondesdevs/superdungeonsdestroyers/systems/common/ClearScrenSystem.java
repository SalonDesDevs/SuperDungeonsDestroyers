package org.salondesdevs.superdungeonsdestroyers.systems.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import net.wytrem.ecs.BaseSystem;

import javax.inject.Singleton;

@Singleton
public class ClearScrenSystem extends BaseSystem {
    @Override
    public void process() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
    }
}
