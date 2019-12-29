package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import net.wytrem.ecs.World;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerName;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.DisconnectReason;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.VersionCheckSuccess;
import org.salondesdevs.superdungeonsdestroyers.states.IngameState;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.MainMenuScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ConnectingToServerNetHandler implements NetworkHandlerSystem.Handler {
    private static final Logger logger = LoggerFactory.getLogger( ConnectingToServerNetHandler.class );

    @Inject
    World world;

    @Inject
    UiSystem uiSystem;

    @Inject
    NetworkSystem networkSystem;

    @Override
    public void handle(Packet packet) {
        logger.info("Received {}", packet.getClass().getSimpleName());
        if (packet instanceof VersionCheckSuccess) {
            if (uiSystem.getCurrentScreen() instanceof MainMenuScreen) {
                networkSystem.send(new PlayerName(((MainMenuScreen) uiSystem.getCurrentScreen()).getNickname()));
            }

            world.push(IngameState.class);
        }
//        else if (packet instanceof DisconnectReason) {
//            // TODO: correct states and screens order
//            if (uiSystem.getCurrentScreen() instanceof MainMenuScreen) {
//                ((MainMenuScreen) uiSystem.getCurrentScreen()).connectFailed(null);
//            }
//        }
    }
}
