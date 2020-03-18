package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.eventbus.Subscribe;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.content.AnimationsCreator;
import org.salondesdevs.superdungeonsdestroyers.library.events.net.PacketReceivedEvent;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.*;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animation;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wytrem.ecs.Component;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.World;

@Singleton
public class IngameNetHandler extends Service {

    private static final Logger logger = LoggerFactory.getLogger( IngameNetHandler.class );

    @Subscribe
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        handle(packetReceivedEvent.getPacket());
    }

    public void handle(Packet packet) {
        logger.debug("Received {}", packet.getClass().getSimpleName());
        if (packet instanceof Welcome) {
            this.handleWelcome(((Welcome) packet));
        }
        else if (packet instanceof EntityTeleport) {
            this.handleEntityTeleport(((EntityTeleport) packet));
        }
        else if (packet instanceof EntityMove) {
            this.handleEntityMove(((EntityMove) packet));
        }
        else if (packet instanceof SwitchLevel) {
            this.handleSwitchLevel(((SwitchLevel) packet));
        }
        else if (packet instanceof EntitySpawn) {
            this.handleEntitySpawn(((EntitySpawn) packet));
        }
        else if (packet instanceof EntityComponentSet){
            this.handleEntityComponentSet(((EntityComponentSet) packet));
        }
        else if (packet instanceof FromServerChat) {
            this.handleFromServerChat((FromServerChat) packet);
        }
    }

    @Inject
    ClientChat clientChat;

    private void handleFromServerChat(FromServerChat packet) {
        clientChat.onMessage(packet.getChatChannel(), packet.getChatMessage());
    }

    @Inject
    World world;

    private void handleEntityComponentSet(EntityComponentSet packet) {
        setFromValue(packet.entityId, packet.watchableComponent);
    }

    private <C extends Component> void setFromValue(int entityId, C component) {
        ((Mapper<C>) world.getMapper(component.getClass())).set(entityId, component);
    }

    private void handleEntitySpawn(EntitySpawn entitySpawn) {
        if (entitySpawn.entityKind.equals(EntityKind.PLAYER)) {
            this.entityCreator.setPlayer(entitySpawn.entityId);
        }
    }

    private void handleSwitchLevel(SwitchLevel switchLevel) {
        this.mapSwitcher.setRoom(switchLevel.level);
    }

    private void handleEntityTeleport(EntityTeleport event) {
        if (positionMapper.has(event.entityId)) {
//            positionMapper.get(event.entityId).set(event.x, mapSwitcher.currentHeight - event.location().y() - 1);
            positionMapper.get(event.entityId).set(event.x, event.y);
        }
        else {
            positionMapper.set(event.entityId, new Position(event.x, event.y));
        }
    }

    @Inject
    Animator animator;

    @Inject
    AnimationsCreator animationsCreator;

    private void handleEntityMove(EntityMove entityMove) {
        if (positionMapper.has(entityMove.entityId)) {
            Position tilePosition = positionMapper.get(entityMove.entityId);

            switch (entityMove.facing) {
                case NORTH:
                    tilePosition.y++;
                    break;
                case SOUTH:
                    tilePosition.y--;
                    break;
                case WEST:
                    tilePosition.x--;
                    break;
                case EAST:
                    tilePosition.x++;
                    break;
            }
        }

        if (positionMapper.has(entityMove.entityId) && offsetMapper.has(entityMove.entityId)) {
            Animation<Float> walkAnimation = animationsCreator.createMoveAnimationBasedOnSpeed(entityMove.entityId, entityMove.facing, () -> {});
            animator.play(walkAnimation);
        }
    }

    private void handleWelcome(Welcome welcome) {
        entityCreator.addLocalPlayer(welcome.me);
    }

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    LevelSwitcher mapSwitcher;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Assets assetService;

    @Inject
    EntityCreator entityCreator;


//    public void handleEnvironment(Environment environment) {
//        logger.trace("Received environment {}", environment);
//
//

//
//
//        for (int i = 0; i < environment.entitiesLength(); i++) {
//            Entity entity = environment.entities(i);
//            int id = (int) entity.entityId();
//
//            if (entity.kindType() == EntityKind.Player) {
//                Player playerMessage = (Player) entity.kind(new Player());
//                if (positionMapper.has(id)) {
//                    positionMapper.get(id).set(playerMessage.location().x(), mapSwitcher.currentHeight - playerMessage.location().y() - 1);
//                }
//                else {
//                    positionMapper.set(id, new Position(playerMessage.location().x(), mapSwitcher.currentHeight - playerMessage.location().y()));
//                }
//            }
//        }
//    }
}
