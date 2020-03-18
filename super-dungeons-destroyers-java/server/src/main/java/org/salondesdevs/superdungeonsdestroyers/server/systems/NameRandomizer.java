package org.salondesdevs.superdungeonsdestroyers.server.systems;

import javax.inject.Inject;

import org.salondesdevs.superdungeonsdestroyers.library.components.Name;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;

public class NameRandomizer extends IteratingSystem {

    public NameRandomizer() {
        super(Aspect.all(PlayerConnection.class));
    }

    @Inject
    Mapper<Name> nameMapper;

    long last = System.currentTimeMillis();

    @Override
    public void process(int entity) {
        if (System.currentTimeMillis() - last > 5000) {
            last = System.currentTimeMillis();

            if (!nameMapper.has(entity)) {
                nameMapper.set(entity, new Name("player " + Math.random()));
            }
            else {
                nameMapper.get(entity).setValue("Hi" + Math.random());
            }
        }
    }
}
