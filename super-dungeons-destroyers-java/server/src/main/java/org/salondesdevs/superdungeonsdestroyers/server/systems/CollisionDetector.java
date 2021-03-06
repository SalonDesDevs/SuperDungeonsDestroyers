package org.salondesdevs.superdungeonsdestroyers.server.systems;

import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.mapeditor.core.Properties;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.events.EntityMoveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Detects tile collisions and cancels {@link EntityMoveEvent} accordingly.
 */
@Singleton
public class CollisionDetector extends Service {

    private static final Logger logger = LoggerFactory.getLogger(CollisionDetector.class);

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    MapLoader mapLoader;

    @EventHandler(priority = EventHandler.Priority.SYSTEM)
    public void onEntityMove(EntityMoveEvent event) {
        int playerId = event.getEntity();
        Position pos = positionMapper.get(playerId);

        int wantedX = pos.x + event.getDirection().x;
        int wantedY = pos.y + event.getDirection().y;
        TileLayer ground = ((TileLayer) mapLoader.map.getLayerByName("ground"));

        Tile tile = ground.getTileAt(wantedX, wantedY);
        if (tile != null) {
            Properties properties = tile.getProperties();

            if (properties != null) {
                if (properties.getProperty("solid", "false").equals("true")) {
                    // If the wanted tile is solid, discard move.
                    logger.warn("Player {} tried to move on a solid tile.", playerId);
                    event.cancel();
                }
            }
        }

    }
}
