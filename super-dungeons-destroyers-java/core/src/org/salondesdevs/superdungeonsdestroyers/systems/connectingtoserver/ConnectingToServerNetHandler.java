package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectingToServerNetHandler implements NetworkHandlerSystem.Handler {
    private static final Logger logger = LoggerFactory.getLogger( ConnectingToServerNetHandler.class );

    @Override
    public void handle(Packet packet) {
        logger.info("Received {}", packet);

    }
}
