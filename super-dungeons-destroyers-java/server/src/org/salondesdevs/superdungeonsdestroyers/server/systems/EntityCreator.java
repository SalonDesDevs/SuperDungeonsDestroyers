package org.salondesdevs.superdungeonsdestroyers.server.systems;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;
import org.salondesdevs.superdungeonsdestroyers.server.components.Tracked;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityCreator extends Service {

    @Inject
    World world;

    @Inject
    Mapper<PlayerConnection> playerConnectionMapper;

    public int createPlayer() {
        int player = world.createEntity();
        addTracked(player);
        addPosition(player);
        addKind(player, EntityKind.PLAYER);

        return player;
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
