package org.salondesdevs.superdungeonsdestroyers.content;

import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Speed;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animation;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Interpolators;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;

@Singleton
public class AnimationsCreator extends Service {

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    Mapper<Speed> speedMapper;


    public Animation<Float> createMoveAnimationBasedOnSpeed(int entity, Facing facing, Runnable runnable) {
        if (speedMapper.has(entity) && offsetMapper.has(entity)) {
            Speed speed = speedMapper.get(entity);
            Offset offset = offsetMapper.get(entity);

            return this.createMoveAnimation(1.f / speed.get(), offset, facing, runnable);
        }
        else {
            return null;
        }
    }

    /**
     * Creates an animation that will modify the proper {@link Offset} field to simulate player movement.
     * This assumes the {@link Position} has already be changed!
     */
    public Animation<Float> createMoveAnimation(float duration, Offset offset, Facing facing, Runnable onEnd) {
        float start = Float.NaN;
        Consumer<Float> setter = null;

        switch (facing) {
            case SOUTH:
                start = 1.0f;
                setter = offset::setY;
                break;
            case NORTH:
                start = -1.0f;
                setter = offset::setY;
                break;
            case WEST:
                start = 1.0f;
                setter = offset::setX;
                break;
            case EAST:
                start = -1.0f;
                setter = offset::setX;
                break;
        }

        return new Animation<>(duration, start, 0.0f, Interpolators.LINEAR_FLOAT, setter, onEnd);
    }
}
