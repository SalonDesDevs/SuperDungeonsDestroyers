package org.salondesdevs.superdungeonsdestroyers.library.systems.animations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.inject.Singleton;

import net.wytrem.ecs.BaseSystem;

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
}
