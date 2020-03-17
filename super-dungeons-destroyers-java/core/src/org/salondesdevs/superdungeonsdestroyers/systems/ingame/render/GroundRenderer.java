package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.BaseSystem;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.LevelSwitcher;

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


}
