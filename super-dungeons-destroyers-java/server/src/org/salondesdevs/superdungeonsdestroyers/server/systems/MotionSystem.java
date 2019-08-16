package org.salondesdevs.superdungeonsdestroyers.server.systems;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityTeleport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MotionSystem extends Service {

    private static final Logger logger = LoggerFactory.getLogger( MotionSystem.class );

    @Inject
    NetworkSystem networkSystem;

    @Inject
    Mapper<Position> positionMapper;

    public void playerMoved(int playerId, PlayerMove playerMove) {
        // TODO: check if the tile is free
        networkSystem.broadcastExcluding(playerId, new EntityMove(playerId, playerMove.direction));
        positionMapper.get(playerId).increment(playerMove.direction);
    }

    public void teleport(int entityId, int x, int y) {
        if (positionMapper.has(entityId)) {
            Position position = positionMapper.get(entityId);
            if (position.x != x || position.y != y) {
                position.x = x;
                position.y = y;
                networkSystem.broadcast(new EntityTeleport(entityId, x, y));
            }
        }
    }
}
