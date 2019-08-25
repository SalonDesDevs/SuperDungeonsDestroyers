package org.salondesdevs.superdungeonsdestroyers.server.systems;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;
import org.checkerframework.checker.units.qual.A;
import org.salondesdevs.superdungeonsdestroyers.library.components.Name;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;

import javax.inject.Inject;

public class NameRandomizer extends IteratingSystem {

    public NameRandomizer() {
        super(Aspect.all(PlayerConnection.class));
    }

    @Inject
    Mapper<Name> nameMapper;


    @Override
    public void process(int entity) {
        if (Math.random() < 0.1) {
            nameMapper.set(entity, new Name("player " + Math.random()));
        }
    }
}
