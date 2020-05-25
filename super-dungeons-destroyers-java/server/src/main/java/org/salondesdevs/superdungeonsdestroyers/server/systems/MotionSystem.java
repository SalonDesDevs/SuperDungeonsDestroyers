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

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MotionSystem extends Service {
    @Inject
    NetworkSystem networkSystem;

    @Inject
    EnvironmentManager environmentManager;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Mapper<Facing> facingMapper;

    @Inject
    EventBus eventBus;

    private boolean tryMove(int entity, Facing direction) {
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

            if (!tryMove(playerId, playerMove.facing)) {
                // The move was discarded.
                Position pos = positionMapper.get(playerId);
                networkSystem.send(playerId, new EntityTeleport(playerId, pos.x, pos.y));
            }
            else {
                // The move was successful, set the facing accordingly.
                facingMapper.set(playerId, playerMove.facing);
            }
        }
    }
}
