package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import SDD.Common.Room;
import SDD.Server.Content;
import SDD.Server.Environment;
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
            case Content.Environment:
                this.handleEnvironment((Environment) message.content(new Environment()));
            default:
                System.out.println("Received message with contentType" + message.contentType());
        }
    }

    private void handleEnvironment(Environment environment) {
        Room room = environment.room();
        int kind = room.kind();
    }
}
