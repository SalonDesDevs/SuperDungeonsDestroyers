package org.salondesdevs.superdungeonsdestroyers.server.systems;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Speed;
import org.salondesdevs.superdungeonsdestroyers.server.components.Tracked;

import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import net.wytrem.ecs.World;

@Singleton
/**
 * This is NOT for direct use, see {@link EnvironmentManager#spawn(EntityKind)}.
 */
public class EntityCreator extends Service {

    @Inject
    World world;

    private int createPlayer() {
        int player = world.createEntity();
        addKind(player, EntityKind.PLAYER);
        addTracked(player);
        addPosition(player);

        return player;
    }

    @Inject
    Mapper<Speed> speedMapper;

    private void addSpeed(int player) {
        speedMapper.set(player, new Speed(10.0f));
    }

    public int create(EntityKind entityKind) {
        switch (entityKind) {
            case PLAYER:
                return createPlayer();
        }

        return 0;
    }

    @Inject
    Mapper<EntityKind> entityKindMapper;

    private void addKind(int player, EntityKind kind) {
        entityKindMapper.set(player, kind);
    }

    @Inject
    Mapper<Position> positionMapper;

    private void addPosition(int player) {

        positionMapper.set(player, new Position());
    }

    @Inject
    Mapper<Tracked> trackedMapper;

    private void addTracked(int entityId) {
        trackedMapper.set(entityId, Tracked.INSTANCE);
    }
}
