package org.salondesdevs.superdungeonsdestroyers.library.components;

import net.wytrem.ecs.Component;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.AutoWatched;

public enum Role implements Component, AutoWatched {
    KNIGHT,
    MAGE,
    BOWMAN;
}
