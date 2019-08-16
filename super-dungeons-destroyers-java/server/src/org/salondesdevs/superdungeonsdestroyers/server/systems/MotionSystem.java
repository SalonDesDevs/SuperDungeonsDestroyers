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
    EnvironmentManager environmentManager;


    public void playerMoved(int playerId, PlayerMove playerMove) {
        // TODO: check if the tile is free

        environmentManager.moveEntity(playerId, playerMove.direction);
    }


}
