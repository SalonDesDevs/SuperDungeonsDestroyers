package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.salondesdevs.superdungeonsdestroyers.library.components.Role;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import net.wytrem.ecs.Mapper;
import org.salondesdevs.superdungeonsdestroyers.components.*;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.ThatsYou;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EntityCreator;
import org.salondesdevs.superdungeonsdestroyers.content.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.PacketReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class EntityCreatorClient extends EntityCreator {

    private static final Logger logger = LoggerFactory.getLogger(EntityCreatorClient.class);

    @Inject
    Mapper<Sprited> spritedMapper;

    @Inject
    Assets assets;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    Mapper<Animated> animatedMapper;

    @Inject
    Mapper<Me> meMapper;

    @Inject
    Mapper<Camera> cameraMapper;

    @Inject
    Mapper<Role> roleMapper;

    @Inject
    Mapper<AnimationsRegistry> animationsRegistryMapper;

    @Override
    public void initialize() {
        super.initialize();

        {
            Map<ActionState, Animation<TextureRegion>> map = new HashMap<>();
            map.put(ActionState.IDLE, assets.bowmanIdle);
            map.put(ActionState.MOVING, assets.bowmanWalk);
            AnimationsRegistry bowman = new AnimationsRegistry(map);

            map = new HashMap<>();
            map.put(ActionState.IDLE, assets.knightIdle);
            map.put(ActionState.MOVING, assets.knightWalk);
            AnimationsRegistry knight = new AnimationsRegistry(map);


            map = new HashMap<>();
            map.put(ActionState.IDLE, assets.mageIdle);
            map.put(ActionState.MOVING, assets.mageWalk);
            AnimationsRegistry mage = new AnimationsRegistry(map);

            roleMapper.addListener(new Mapper.ChangeListener<Role>() {
                @Override
                public void onSet(int entity, Role oldValue, Role newValue) {
                    if (newValue == Role.BOWMAN) {
                        animationsRegistryMapper.set(entity, bowman);
                    }
                    else if (newValue == Role.KNIGHT) {
                        animationsRegistryMapper.set(entity, knight);
                    }
                    else if (newValue == Role.MAGE) {
                        animationsRegistryMapper.set(entity, mage);
                    }
                }

                @Override
                public void onUnset(int entity, Role oldValue) {

                }
            });
        }
    }

    public void addSprited(int entity, Sprited.Sprites sprites) {
        this.spritedMapper.set(entity, new Sprited(sprites.toTextureRegion(assets.tileset)));
    }

    @Inject
    Mapper<ActionState> actionStateMapper;

    @Override
    public void addBasicComponents(int entity) {
        super.addBasicComponents(entity);
        actionStateMapper.set(entity, ActionState.IDLE);
        offsetMapper.set(entity, new Offset());
    }

    @Override
    public void addPlayerComponents(int entity) {
        super.addPlayerComponents(entity);
    }

    public void addLocalPlayer(int me) {
        cameraMapper.set(me, Camera.INSTANCE);
        meMapper.set(me, Me.INSTANCE);
    }


    @EventHandler
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof ThatsYou) {
            addLocalPlayer(((ThatsYou) packetReceivedEvent.getPacket()).me);
        }
    }
}
