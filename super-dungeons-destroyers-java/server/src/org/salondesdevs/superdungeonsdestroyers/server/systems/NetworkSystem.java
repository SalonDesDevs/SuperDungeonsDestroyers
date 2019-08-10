package org.salondesdevs.superdungeonsdestroyers.server.systems;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.wytrem.ecs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

public class NetworkSystem extends BaseSystem {
    private static final Logger logger = LoggerFactory.getLogger( NetworkSystem.class );

    @Inject
    org.salondesdevs.superdungeonsdestroyers.server.Server sddServer;

    @Override
    public void initialize() {

        Server server = new Server();
        Packet.get
        server.start();

        try {
            server.bind(9000);
        } catch (IOException e) {
            logger.error("Could not start kryonet server, aborting: ", e);
            sddServer.stop();
        }

        server.addListener(new NetListener());
    }

    private class NetListener extends Listener {
        @Override
        public void received(Connection connection, Object object) {
            logger.info("Received {}", object);
        }

        @Override
        public void connected(Connection connection) {
        }
    }
}
