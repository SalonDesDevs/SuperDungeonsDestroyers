package org.salondesdevs.superdungeonsdestroyers.components;

import net.wytrem.ecs.Component;

/**
 * Marks the client player entity.
 */
public class Me implements Component {
    public static final Me INSTANCE = new Me();

    private Me() {

    }
}
