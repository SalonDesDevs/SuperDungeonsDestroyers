package org.salondesdevs.superdungeonsdestroyers.library.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    SubscriberPriority priority() default SubscriberPriority.NORMAL;

    boolean ignoreCancelled() default false;
}
