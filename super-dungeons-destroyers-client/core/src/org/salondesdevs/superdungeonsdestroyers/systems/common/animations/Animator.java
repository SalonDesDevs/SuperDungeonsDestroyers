package org.salondesdevs.superdungeonsdestroyers.systems.common.animations;

import net.wytrem.ecs.*;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

    public void submit(Animation<?> animation) {
        this.animations.add(animation);
    }
}
