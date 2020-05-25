package org.salondesdevs.superdungeonsdestroyers.server.systems;

import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.components.MaxHealth;
import org.salondesdevs.superdungeonsdestroyers.library.components.Name;
import org.salondesdevs.superdungeonsdestroyers.library.components.Size;
import org.salondesdevs.superdungeonsdestroyers.library.components.Speed;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerName;
import org.salondesdevs.superdungeonsdestroyers.server.events.PacketReceivedEvent;
import org.salondesdevs.superdungeonsdestroyers.server.events.PlayerJoinedEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerComponentsInitializer extends Service {

    @Inject
    Mapper<MaxHealth> maxHealthMapper;

    @Inject
    EnvironmentManager environmentManager;

    @Inject
    Mapper<Size> sizeMapper;

    @Inject
    Mapper<Speed> speedMapper;

    @Inject
    ChatSystem chatSystem;

    @EventHandler(priority = EventHandler.Priority.SYSTEM)
    public void onPlayerJoined(PlayerJoinedEvent playerJoinedEvent) {
        int player = playerJoinedEvent.getPlayer();
        maxHealthMapper.set(player, new MaxHealth(100));
        environmentManager.teleport(player, 1, 1);
        sizeMapper.set(player, new Size(1.0f, 1.3f));
        speedMapper.set(player, new Speed(3.0f));
    }

    @Inject
    Mapper<Name> nameMapper;

    @EventHandler
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof PlayerName) {
            PlayerName playerName = (PlayerName) packetReceivedEvent.getPacket();
            nameMapper.set(packetReceivedEvent.getPlayer(), new Name(playerName.getName()));
        }
    }
}
