package org.salondesdevs.superdungeonsdestroyers.server.systems;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.Direction;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntitySpawn;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityTeleport;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.SwitchLevel;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.Welcome;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Levels;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;
import org.salondesdevs.superdungeonsdestroyers.server.components.Tracked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
/**
 * Used to keep players up to date (entities movements, levels changes...).
 */
public class Synchronizer extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger( Synchronizer.class );

    public Synchronizer() {
        super(Aspect.all(Tracked.class));
    }

    @Override
    public void process(int entity) {

    }

    @Inject
    Mapper<PlayerConnection> playerConnectionMapper;

    @Inject
    NetworkSystem networkSystem;

    Levels level = Levels.BOTTOM;

    @Inject
    Mapper<EntityKind> entityKindMapper;

    @Inject
    Mapper<Position> positionMapper;

    public void startSynchronizingWith(int player) {
        playerConnectionMapper.get(player).send(new SwitchLevel(this.level), new Welcome(player));
        sendTrackedEntities(player);
    }

    private void sendTrackedEntities(int player) {
        List<Packet> packetList = new ArrayList<>((entities.size() - 1) * 2);

        for (int i = 0; i < entities.size(); i++) {
            if (entities.getInt(i) != player) {
                addEntityData(entities.getInt(i), packetList);
            }
        }
        playerConnectionMapper.get(player).sendAll(packetList);
    }

    private void addEntityData(int entityId, List<Packet> packetList) {
        if (entityKindMapper.has(entityId)) {
            packetList.add(new EntitySpawn(entityId, entityKindMapper.get(entityId)));

            if (positionMapper.has(entityId)) {
                packetList.add(new EntityTeleport(entityId, positionMapper.get(entityId)));
            }
        }
    }

    public void notifyEntitySpawned(int entity, EntityKind entityKind) {
        networkSystem.broadcast(new EntitySpawn(entity, entityKind));
    }

    public void notifyEntityMoved(int entity, Direction direction) {
        if (entityKindMapper.get(entity).equals(EntityKind.PLAYER)) {
            networkSystem.broadcastExcluding(entity, new EntityMove(entity, direction));
        }
        else {
            networkSystem.broadcast(new EntityMove(entity, direction));
        }
    }

    public void notifyPositionChanged(int entity, int x, int y) {
        networkSystem.broadcast(new EntityTeleport(entity, x, y));
    }
}
