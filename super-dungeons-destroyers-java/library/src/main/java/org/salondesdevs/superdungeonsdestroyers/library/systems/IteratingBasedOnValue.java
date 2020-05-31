package org.salondesdevs.superdungeonsdestroyers.library.systems;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.Component;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;

import javax.inject.Inject;

public abstract class IteratingBasedOnValue<C extends Component> extends IteratingSystem {
    protected final C value;

    public IteratingBasedOnValue(Class<C> clazz, C value) {
        super(Aspect.all(clazz));
        this.value = value;
    }

    @Inject
    Mapper<C> mapper;

    @Override
    public void initialize() {
        mapper.addListener(new Mapper.ChangeListener<C>() {
            @Override
            public void onSet(int entity, C oldValue, C newValue) {
                notifyAspectChanged(entity);
            }

            @Override
            public void onUnset(int entity, C oldValue) {
                notifyAspectChanged(entity);
            }
        });
    }

    @Override
    public void notifyAspectChanged(int entity) {
        if (this.entities.contains(entity)) {
            if (!this.world.matches(entity, this.aspect)) {
                this.entities.rem(entity);
            }
        } else {
            if (this.world.matches(entity, this.aspect)) {
                this.entities.add(entity);
            }
        }
    }

    protected boolean matches(int entity) {
        return this.world.matches(entity, this.aspect) && mapper.get(entity).equals(this.value);
    }
}
