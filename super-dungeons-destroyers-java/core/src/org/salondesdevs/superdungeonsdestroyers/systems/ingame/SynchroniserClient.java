package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import net.wytrem.ecs.Component;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import net.wytrem.ecs.World;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.content.AnimationsCreator;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.*;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animation;
import org.salondesdevs.superdungeonsdestroyers.library.systems.animations.Animator;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.PacketReceivedEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Listens to the server packets and keeps entities and components up to date.
 */
@Singleton
public class SynchroniserClient extends Service {

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    LevelSwitcher mapSwitcher;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Assets assetService;

    @Inject
    EntityCreatorClient entityCreatorClient;

    @Inject
    World world;

    @Inject
    Animator animator;

    @Inject
    AnimationsCreator animationsCreator;

    @EventHandler
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        Packet packet = packetReceivedEvent.getPacket();
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
    }

    private void handleEntityComponentSet(EntityComponentSet packet) {
        setFromValue(packet.entityId, packet.watchableComponent);
    }

    private <C extends Component> void setFromValue(int entityId, C component) {
        ((Mapper<C>) world.getMapper(component.getClass())).set(entityId, component);
    }

    private void handleEntitySpawn(EntitySpawn entitySpawn) {
        this.entityCreatorClient.addComponents(entitySpawn.entityId, entitySpawn.entityKind);
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
        entityCreatorClient.addLocalPlayer(welcome.me);
    }
}
