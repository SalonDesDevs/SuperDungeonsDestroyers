package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import net.wytrem.ecs.BaseSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.LevelSwitcher;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GroundRenderer extends BaseSystem {

    @Inject
    MapLayerRenderer mapLayerRenderer;

    @Inject
    LevelSwitcher levelSwitcher;

    @Override
    public void initialize() {
        mapLayerRenderer.initialize("ground");
    }

    @Override
    public void process() {
        this.mapLayerRenderer.render(levelSwitcher.getTerrain());
    }

    @Override
    public boolean isEnabled() {
        return this.levelSwitcher.getTerrain() != null;
    }
}
