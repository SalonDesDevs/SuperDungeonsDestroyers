package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import SDD.Server.Event;
import SDD.Server.Events;
import com.google.inject.Injector;
import net.wytrem.ecs.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class NetworkHandlerSystem extends BaseSystem {

    List<Events> messagesToHandle = new ArrayList<>();

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

    private void handleMessages(Events messages) {
        for (int i = 0; i < messages.eventsLength(); i++) {
            Event message = messages.events(i);

            this.currentHandler.handle(message);
        }
    }

    public interface Handler {
        void handle(Event message);
    }
}
