package org.salondesdevs.superdungeonsdestroyers.server.systems;

import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import net.wytrem.ecs.World;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Role;
import org.salondesdevs.superdungeonsdestroyers.library.components.Speed;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EntityCreator;
import org.salondesdevs.superdungeonsdestroyers.server.components.Tracked;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityCreatorServer extends EntityCreator {

    @Inject
    Mapper<Role> roleMapper;

    @Override
    protected void addPlayerComponents(int entity) {
        super.addPlayerComponents(entity);
        addTracked(entity);

        roleMapper.set(entity, Role.BOWMAN);
    }

    @Inject
    Mapper<Tracked> trackedMapper;

    private void addTracked(int entity) {
        trackedMapper.set(entity, Tracked.INSTANCE);
    }
}
