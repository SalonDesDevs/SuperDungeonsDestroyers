package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import net.wytrem.ecs.World;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.VersionCheckSuccess;
import org.salondesdevs.superdungeonsdestroyers.states.IngameState;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ConnectingToServerNetHandler implements NetworkHandlerSystem.Handler {
    private static final Logger logger = LoggerFactory.getLogger( ConnectingToServerNetHandler.class );

    @Inject
    World world;

    @Override
    public void handle(Packet packet) {
        logger.info("Received {}", packet);
        if (packet instanceof VersionCheckSuccess) {
            world.push(IngameState.class);
        }
    }
}
