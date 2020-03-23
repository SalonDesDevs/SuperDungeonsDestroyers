package org.salondesdevs.superdungeonsdestroyers.library.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    Priority priority() default Priority.NORMAL;

    boolean ignoreCancelled() default false;

    enum Priority {
        SYSTEM,
        VERY_HIGH,
        HIGH,
        NORMAL,
        LOW,
        VERY_LOW;
    }
}
