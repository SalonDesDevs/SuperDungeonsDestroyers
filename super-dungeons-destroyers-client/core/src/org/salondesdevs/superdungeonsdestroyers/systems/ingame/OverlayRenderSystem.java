package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import javax.inject.Singleton;

/**
 * If a {@link org.salondesdevs.superdungeonsdestroyers.components.Terrain} entity is present, will use it to render the
 * map layer called "overlay".
 */
@Singleton
public class OverlayRenderSystem extends MapLayerRenderSystem {
    public OverlayRenderSystem() {
        super("overlay");
    }
}
