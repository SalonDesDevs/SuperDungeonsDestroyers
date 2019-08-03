package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import SDD.Server.Event;
import SDD.Server.EventUnion;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectingToServerNetHandler implements NetworkHandlerSystem.Handler {
    private static final Logger logger = LoggerFactory.getLogger( ConnectingToServerNetHandler.class );

    @Override
    public void handle(Event message) {
        logger.info("Received event with type={}", EventUnion.name(message.eventType()));

//        switch (message.contentType()) {
//            case Content
//                    .Pong:
//                Pong pong = (Pong) message.content(new Pong());
//                System.out.println("Received Pong with value " + pong.value());
//                break;
//            case Content.Environment:
//                break;
//            default:
//                System.out.println("Received message with contentType" + message.contentType());
//        }
    }
}
