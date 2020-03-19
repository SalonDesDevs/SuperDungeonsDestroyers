package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import net.wytrem.ecs.BaseSystem;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
/**
 * Dispatches incoming {@link Packet}s as {@link org.salondesdevs.superdungeonsdestroyers.library.events.Event}s.
 */
public class NetworkHandlerSystem extends BaseSystem {

    private static final Logger logger = LoggerFactory.getLogger(NetworkHandlerSystem.class);

    private List<Packet> messagesToHandle = new ArrayList<>();

    @Inject
    EventBus eventBus;

    @Override
    public void process() {
        synchronized (messagesToHandle) {
            messagesToHandle.forEach(this::handleMessages);
            messagesToHandle.clear();
        }
    }

    public void enqueue(Packet incoming) {
        synchronized (messagesToHandle) {
            messagesToHandle.add(incoming);
        }
    }

    private void handleMessages(Packet packet) {
        eventBus.post(new PacketReceivedEvent(packet));
    }
}
