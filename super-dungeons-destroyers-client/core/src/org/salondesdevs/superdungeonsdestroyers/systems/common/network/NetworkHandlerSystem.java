package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import SDD.Common.Room;
import SDD.Server.Content;
import SDD.Server.Environment;
import SDD.Server.Message;
import SDD.Server.Messages;
import SDD.Server.Pong;
import com.google.inject.Injector;
import net.wytrem.ecs.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class NetworkHandlerSystem extends BaseSystem {

    List<Messages> messagesToHandle = new ArrayList<>();

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

    private void handleMessages(Messages messages) {
        for (int i = 0; i < messages.messagesLength(); i++) {
            Message message = messages.messages(i);

            this.currentHandler.handle(message);
        }
    }

    public interface Handler {
        void handle(Message message);
    }
}
