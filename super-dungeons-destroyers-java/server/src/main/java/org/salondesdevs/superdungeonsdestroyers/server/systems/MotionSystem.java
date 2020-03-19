package org.salondesdevs.superdungeonsdestroyers.server.systems;

import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;
import net.wytrem.ecs.Mapper;
import org.mapeditor.core.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityTeleport;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;
import org.salondesdevs.superdungeonsdestroyers.server.systems.net.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.server.events.PacketReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wytrem.ecs.Service;

public class MotionSystem extends Service {
    private static final Logger logger = LoggerFactory.getLogger( MotionSystem.class );

    @Inject
    NetworkSystem networkSystem;

    @Inject
    EnvironmentManager environmentManager;

    @Inject
    MapLoader mapLoader;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Mapper<PlayerConnection> playerConnectionMapper;


    @Subscribe
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof PlayerMove) {
            PlayerMove playerMove = ((PlayerMove) packetReceivedEvent.getPacket());
            int playerId = packetReceivedEvent.getPlayer();
            Position pos = positionMapper.get(playerId);

            int wantedX = pos.x + playerMove.facing.x;
            int wantedY = pos.y + playerMove.facing.y;
            TileLayer ground = ((TileLayer) mapLoader.map.getLayerByName("ground"));

            Tile tile = ground.getTileAt(wantedX, wantedY);
            if (tile != null) {
                Properties properties = tile.getProperties();

                if (properties != null) {
                    if (properties.getProperty("solid", "false").equals("true")) {
                        // If the wanted tile is solid, discard move.
                        logger.warn("Player {} tried to move on a solid tile.", playerId);
                        playerConnectionMapper.get(playerId).send(new EntityTeleport(playerId, pos.x, pos.y));
                    }
                    else {
                        environmentManager.moveEntity(playerId, playerMove.facing);
                    }
                }
            }
        }
    }
}
