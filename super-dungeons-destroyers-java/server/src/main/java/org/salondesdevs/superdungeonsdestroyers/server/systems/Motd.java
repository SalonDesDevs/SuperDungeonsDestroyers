package org.salondesdevs.superdungeonsdestroyers.server.systems;

import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Version;
import org.salondesdevs.superdungeonsdestroyers.server.events.PlayerJoinedEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Motd extends Service {

    @Inject
    ChatSystem chatSystem;

    @EventHandler
    public void onPlayerJoined(PlayerJoinedEvent event) {
        chatSystem.send(event.getPlayer(), "Welcome on the SDD server v" + Version.VERSION);
    }
}
