package org.salondesdevs.superdungeonsdestroyers.server.systems;

import org.salondesdevs.superdungeonsdestroyers.library.components.watched.AutoWatched;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.WatchableComponent;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.AutoWatchedComponents;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Levels;
import org.salondesdevs.superdungeonsdestroyers.server.components.Tracked;
import org.salondesdevs.superdungeonsdestroyers.server.events.PlayerJoinedEvent;
import org.salondesdevs.superdungeonsdestroyers.server.systems.net.NetworkSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Used to keep players up to date (entities movements, levels changes...).
 */
@Singleton
public class Synchronizer extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger( Synchronizer.class );

    public Synchronizer() {
        super(Aspect.all(Tracked.class));
    }

    @Inject
    World world;
    
    private Set<Mapper<? extends AutoWatched>> autoWatchedMappers;

    @Inject
    Mapper<Tracked> trackedMapper;

    @Override
    public void initialize() {
        this.autoWatchedMappers = new HashSet<>();
        world.addMapperRegisterListener(mapper -> {
            Class<? extends Component> clazz = mapper.getComponentTypeClass();
            if (AutoWatchedComponents.contains(clazz) && AutoWatched.class.isAssignableFrom(clazz)) {
                Mapper<? extends AutoWatched> casted =((Mapper<? extends AutoWatched>) mapper);

                this.autoWatchedMappers.add(casted);
                casted.addListener(new WatchedComponentChanged());
            }
        });

        trackedMapper.addListener(new Mapper.ChangeListener<Tracked>() {
            @Override
            public void onSet(int entity, Tracked oldValue, Tracked newValue) {
               if (oldValue == null) {
                   onTrackedSet(entity);
               }
            }

            @Override
            public void onUnset(int entity, Tracked oldValue) {
                onTrackedUnset(entity);
            }
        });
    }

    @Override
    public void process(int entity) {
        for (Mapper<? extends Component> mapper : autoWatchedMappers) {
            if (mapper.has(entity) && WatchableComponent.class.isAssignableFrom(mapper.getComponentTypeClass())) {
                WatchableComponent watchableComponent = (WatchableComponent) mapper.get(entity);
                if (watchableComponent.hasChanged()) {
                    this.sendComponentUpdate(entity, watchableComponent);
                    watchableComponent.setChanged(false);
                }
            }
        }
    }
    
    @Inject
    NetworkSystem networkSystem;

    Levels level = Levels.TOP;

    @Inject
    Mapper<EntityKind> entityKindMapper;

    @Inject
    Mapper<Position> positionMapper;

    @EventHandler
    public void onPlayerJoined(PlayerJoinedEvent playerJoinedEvent) {
        this.startSynchronizingWith(playerJoinedEvent.getPlayer());
    }

    public void startSynchronizingWith(int player) {
        sendTrackedEntities(player);

        networkSystem.send(player, new SwitchLevel(this.level), new ThatsYou(player));
    }

    private void sendTrackedEntities(int player) {
        List<Packet> packetList = new ArrayList<>((entities.size() - 1) * 6);

        for (int i = 0; i < entities.size(); i++) {
            addEntityData(entities.getInt(i), packetList);
        }

        networkSystem.send(player, packetList);
    }

    private void addEntityData(int entity, List<Packet> packetList) {
        if (entityKindMapper.has(entity)) {
            packetList.add(new EntitySpawn(entity, entityKindMapper.get(entity)));

            if (positionMapper.has(entity)) {
                packetList.add(new EntityTeleport(entity, positionMapper.get(entity)));
            }

            for (Mapper<? extends AutoWatched> mapper : autoWatchedMappers) {
                if (mapper.has(entity)) {
                    packetList.add(new EntityComponentSet(entity, mapper.get(entity)));
                }
            }
        }
    }

    private void onTrackedSet(int entity) {
        networkSystem.broadcast(new EntitySpawn(entity, entityKindMapper.get(entity)));
    }

    private void onTrackedUnset(int entity) {
        networkSystem.broadcast(new EntityDespawn(entity));
    }

    public void startTracking(int entity) {
        trackedMapper.set(entity, Tracked.INSTANCE);
    }

    public void stopTracking(int entity) {
        trackedMapper.unset(entity);
    }

    public void notifyEntityMoved(int entity, Facing facing) {
        if (entityKindMapper.get(entity).equals(EntityKind.PLAYER)) {
            networkSystem.broadcastExcluding(entity, new EntityMove(entity, facing));
        }
        else {
            networkSystem.broadcast(new EntityMove(entity, facing));
        }
    }

    public void notifyPositionChanged(int entity, int x, int y) {
        networkSystem.broadcast(new EntityTeleport(entity, x, y));
    }
    
    private void sendComponentUpdate(int entity, AutoWatched newValue) {
        networkSystem.broadcast(new EntityComponentSet(entity, newValue));
    }

    private void sendComponentUnset(int entity, AutoWatched oldValue) {
        networkSystem.broadcast(new EntityComponentUnset(entity, AutoWatchedComponents.getId(oldValue)));
    }

    class WatchedComponentChanged implements Mapper.ChangeListener<AutoWatched> {
        @Override
        public void onSet(int entity, AutoWatched oldValue, AutoWatched newValue) {
            sendComponentUpdate(entity, newValue);
        }

        @Override
        public void onUnset(int entity, AutoWatched oldValue) {
            sendComponentUnset(entity, oldValue);
        }
    }
}
