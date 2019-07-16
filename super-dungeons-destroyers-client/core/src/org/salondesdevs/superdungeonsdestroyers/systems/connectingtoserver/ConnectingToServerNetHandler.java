package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import SDD.Server.Content;
import SDD.Server.Environment;
import SDD.Server.Message;
import SDD.Server.Pong;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;

public class ConnectingToServerNetHandler implements NetworkHandlerSystem.Handler {
    @Override
    public void handle(Message message) {
        switch (message.contentType()) {
            case Content
                    .Pong:
                Pong pong = (Pong) message.content(new Pong());
                System.out.println("Received Pong with value " + pong.value());
                break;
            case Content.Environment:
                break;
            default:
                System.out.println("Received message with contentType" + message.contentType());
        }
    }
}
