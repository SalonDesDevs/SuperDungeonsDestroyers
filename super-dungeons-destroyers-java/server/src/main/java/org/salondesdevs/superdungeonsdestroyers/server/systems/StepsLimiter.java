package org.salondesdevs.superdungeonsdestroyers.server.systems;

import com.google.common.eventbus.Subscribe;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.components.Health;
import org.salondesdevs.superdungeonsdestroyers.library.components.RemainingSteps;
import org.salondesdevs.superdungeonsdestroyers.library.events.EntityMoveEvent;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StepsLimiter extends Service {

    @Inject
    Mapper<RemainingSteps> remainingStepsMapper;

    @Inject
    Mapper<Health> healthMapper;

    @EventHandler
    public void onPlayerMove(EntityMoveEvent entityMoveEvent) {
        int entity = entityMoveEvent.getEntity();

        if (remainingStepsMapper.has(entity)) {
            RemainingSteps remainingSteps = remainingStepsMapper.get(entity);

            if (remainingSteps.get() == 0) {

                System.out.println(healthMapper.has(entity));

                if (healthMapper.has(entity)) {
                    // TODO: change the value of decrement on a component basis
                    healthMapper.get(entity).decrement();
                }
                else {
                    entityMoveEvent.cancel();
                }
            }
            else {
                remainingSteps.decrement();
            }
        }
    }
}
