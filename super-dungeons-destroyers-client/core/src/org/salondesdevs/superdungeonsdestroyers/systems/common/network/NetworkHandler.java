package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import SDD.Server.Content;
import SDD.Server.Message;
import SDD.Server.Messages;
import SDD.Server.Pong;
import net.wytrem.ecs.*;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class NetworkHandler extends BaseSystem {

    List<Messages> messagesToHandle = new ArrayList<>();


    @Override
    public void process() {
        synchronized (messagesToHandle) {
            messagesToHandle.forEach(this::handleMessages);
            messagesToHandle.clear();
        }
    }

    private void handleMessages(Messages messages) {
        for (int i = 0; i < messages.messagesLength(); i++) {
            Message message = messages.messages(i);

            handleMessage(message);
        }
    }

    private void handleMessage(Message message) {
        switch (message.contentType()) {
            case Content
                    .Pong:
                Pong pong = (Pong) message.content(new Pong());
                System.out.println("Received Pong with value " + pong.value());
            break;
            default:
                System.out.println("Received message with contentType" + message.contentType());
        }
    }
}
