package org.salondesdevs.superdungeonsdestroyers.server.systems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Facing;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.WatchableComponent;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityComponentSet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntitySpawn;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.EntityTeleport;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.SwitchLevel;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.Welcome;
import org.salondesdevs.superdungeonsdestroyers.library.utils.AutoWatchedComponents;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Levels;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;
import org.salondesdevs.superdungeonsdestroyers.server.components.Tracked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.Component;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.World;

@Singleton
/**
 * Used to keep players up to date (entities movements, levels changes...).
 */
public class Synchronizer extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger( Synchronizer.class );

    public Synchronizer() {
        super(Aspect.all(Tracked.class));
    }

    @Inject
    World world;
    
    private Set<Mapper<? extends Component>> autoWatchedMappers;

    @Override
    public void initialize() {
        this.autoWatchedMappers = new HashSet<>();
        world.addMapperRegisterListener(mapper -> {
            Class<? extends Component> clazz = mapper.getComponentTypeClass();
            if (AutoWatchedComponents.contains(clazz)) {
                this.autoWatchedMappers.add(mapper);
                mapper.addListener(new WatchedComponentChanged());
            }
        });
    }

    @Override
    public void process(int entity) {
        for (Mapper<? extends Component> mapper : autoWatchedMappers) {
            if (mapper.has(entity)) {
                WatchableComponent watchableComponent = (WatchableComponent) mapper.get(entity);
                if (watchableComponent.hasChanged()) {
                    this.sendComponentUpdate(entity, watchableComponent);
                    watchableComponent.setChanged(false);
                }
            }
        }
    }
    
    @Inject
    Mapper<PlayerConnection> playerConnectionMapper;

    @Inject
    NetworkSystem networkSystem;

    Levels level = Levels.COLLISIONS_TESTER;

    @Inject
    Mapper<EntityKind> entityKindMapper;

    @Inject
    Mapper<Position> positionMapper;

    public void startSynchronizingWith(int player) {
        sendTrackedEntities(player);
        playerConnectionMapper.get(player).send(new SwitchLevel(this.level), new Welcome(player));
    }

    private void sendTrackedEntities(int player) {
        List<Packet> packetList = new ArrayList<>((entities.size() - 1) * 6);

        for (int i = 0; i < entities.size(); i++) {
            addEntityData(entities.getInt(i), packetList);
        }
        playerConnectionMapper.get(player).sendAll(packetList);
    }

    private void addEntityData(int entity, List<Packet> packetList) {
        if (entityKindMapper.has(entity)) {
            packetList.add(new EntitySpawn(entity, entityKindMapper.get(entity)));

            if (positionMapper.has(entity)) {
                packetList.add(new EntityTeleport(entity, positionMapper.get(entity)));
            }

            for (Mapper<? extends Component> mapper : autoWatchedMappers) {
                if (mapper.has(entity)) {
                    packetList.add(new EntityComponentSet(entity, mapper.get(entity)));
                }
            }
        }
    }

    public void notifyEntitySpawned(int entity, EntityKind entityKind) {
        networkSystem.broadcast(new EntitySpawn(entity, entityKind));
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
    
    private void sendComponentUpdate(int entity, Component newValue) {
        networkSystem.broadcast(new EntityComponentSet(entity, newValue));
    }

    class WatchedComponentChanged implements Mapper.ChangeListener<Component> {
        @Override
        public void onSet(int entity, Component oldValue, Component newValue) {
            sendComponentUpdate(entity, newValue);
        }

        @Override
        public void onUnset(int entity, Component oldValue) {
            // TODO
        }
    }
}
