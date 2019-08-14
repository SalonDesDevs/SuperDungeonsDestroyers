package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import com.google.inject.Injector;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class NetworkHandlerSystem extends BaseSystem {

    List<Packet> messagesToHandle = new ArrayList<>();

    @Inject
    Injector injector;

    private Handler currentHandler;

    @Override
    public void process() {
        synchronized (messagesToHandle) {
            if (currentHandler != null) {
                messagesToHandle.forEach(this::handleMessages);
                messagesToHandle.clear();
            }
        }
    }

    public void setCurrentHandler(Class<? extends Handler> clazz) {
        this.currentHandler = injector.getInstance(clazz);
    }

    private void handleMessages(Packet packet) {
            this.currentHandler.handle(packet);
    }

    public interface Handler {
        void handle(Packet packet);
    }
}
