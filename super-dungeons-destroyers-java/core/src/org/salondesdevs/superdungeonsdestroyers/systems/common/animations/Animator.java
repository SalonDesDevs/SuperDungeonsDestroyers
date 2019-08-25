package org.salondesdevs.superdungeonsdestroyers.systems.common.animations;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

@Singleton
public class Animator extends BaseSystem {

    private Collection<Animation<?>> animations;

    @Override
    public void initialize() {
        this.animations = new ArrayList<>(32);
    }

    @Override
    public void process() {
        Iterator<Animation<?>> iterator = this.animations.iterator();

        while (iterator.hasNext()) {
            Animation<?> anim = iterator.next();

            anim.tick(world.getDelta());

            if (anim.isFinished()) {
                iterator.remove();
            }
        }

    }

    public void play(Animation<?> animation) {
        this.animations.add(animation);
    }

    /**
     * Creates an animation that will modify the proper {@link Offset} field to simulate player movement.
     * This assumes the {@link Position} has already be changed!
     */
    public Animation<Float> createMoveAnimation(Offset offset, Facing facing, Runnable onEnd) {
        float start = 0.0f;
        Consumer<Float> setter = null;

        switch (facing) {
            case SOUTH:
                start = 16.0f;
                setter = offset::setY;
                break;
            case NORTH:
                start = -16.0f;
                setter = offset::setY;
                break;
            case WEST:
                start = 16.0f;
                setter = offset::setX;
                break;
            case EAST:
                start = -16.0f;
                setter = offset::setX;
                break;
        }

        return new Animation<>(0.25f, start, 0.0f, Interpolators.LINEAR_FLOAT, setter, onEnd);
    }
}
