package org.salondesdevs.superdungeonsdestroyers.states;

import com.badlogic.gdx.Gdx;
import org.salondesdevs.superdungeonsdestroyers.library.SDDState;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ClearScrenSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.loadingassets.AssetsLoadingSystem;

public class LoadingAssets extends SDDState {
    public LoadingAssets() {
        super();
        register(Assets.class);
        register(ClearScrenSystem.class);
        register(AssetsLoadingSystem.class);
    }

    @Override
    public void pushed() {
        super.pushed();
    }

    @Override
    public void poped() {
        super.poped();
        resume();
    }

    @Override
    public void resume() {
        super.resume();
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
    }

    @Override
    public void pause() {
        super.pause();
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public String toString() {
        return "LoadingAssets";
    }
}
