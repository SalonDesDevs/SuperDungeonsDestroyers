package org.salondesdevs.superdungeonsdestroyers.library.components;

import org.salondesdevs.superdungeonsdestroyers.library.components.watched.FloatWatchedComponent;

/**
 * The walking speed of an entity, in grid unit per second.
 */
public class Speed extends FloatWatchedComponent {
    public Speed() {
        super(2.0f);
    }
}
