package org.salondesdevs.superdungeonsdestroyers.server.systems;

import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.events.EntityMoveEvent;
import org.salondesdevs.superdungeonsdestroyers.library.events.core.EventBus;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityTeleport;
import org.salondesdevs.superdungeonsdestroyers.server.events.PacketReceivedEvent;
import org.salondesdevs.superdungeonsdestroyers.server.systems.net.NetworkSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MotionSystem extends Service {
    private static final Logger logger = LoggerFactory.getLogger(MotionSystem.class);

    @Inject
    NetworkSystem networkSystem;

    @Inject
    EnvironmentManager environmentManager;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    EventBus eventBus;

    public boolean tryMove(int entity, Facing direction) {
        EntityMoveEvent entityMoveEvent = new EntityMoveEvent(entity, direction);

        eventBus.post(entityMoveEvent);

        if (!entityMoveEvent.isCancelled()) {
            environmentManager.moveEntity(entity, direction);
            return true;
        }

        return false;
    }

    @EventHandler
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof PlayerMove) {
            PlayerMove playerMove = ((PlayerMove) packetReceivedEvent.getPacket());
            int playerId = packetReceivedEvent.getPlayer();
            Position pos = positionMapper.get(playerId);
            if (!tryMove(playerId, playerMove.facing)) {
                // If the wanted tile is solid, discard move.
                logger.warn("Player {} tried to move on a solid tile.", playerId);

                networkSystem.send(playerId, new EntityTeleport(playerId, pos.x, pos.y));
            }
        }
    }
}
