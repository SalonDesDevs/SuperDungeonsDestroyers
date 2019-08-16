package org.salondesdevs.superdungeonsdestroyers.server.systems;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
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
 * Used to keep player up to date (entities movements, levels changes...).
 */
public class EnvironmentTracker extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger( EnvironmentTracker.class );

    public EnvironmentTracker() {
        super(Aspect.all(Tracked.class));
    }

    @Override
    public void process(int entity) {

    }

    @Inject
    Mapper<PlayerConnection> playerConnectionMapper;

    @Inject
    MotionSystem motionSystem;

    @Inject
    NetworkSystem networkSystem;

    Levels level = Levels.BOTTOM;

    public void onPlayerJoin(int player) {
        logger.info("onPlayerJoin({})", player);
        playerConnectionMapper.get(player).send(new SwitchLevel(this.level), new Welcome(player));
        sendTrackedEntities(player);
        networkSystem.broadcastExcluding(player, new EntitySpawn(player, EntityKind.PLAYER));
        motionSystem.teleport(player, 1, 1);
    }

    @Inject
    Mapper<EntityKind> entityKindMapper;

    @Inject
    Mapper<Position> positionMapper;

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
}
