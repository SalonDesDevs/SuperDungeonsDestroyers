package org.salondesdevs.superdungeonsdestroyers.states;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.loadingassets.AssetsLoadingSystem;

public class LoadingAssets extends GameState {
    public LoadingAssets() {
        super();
        register(Assets.class);
        register(AssetsLoadingSystem.class);
    }
}
