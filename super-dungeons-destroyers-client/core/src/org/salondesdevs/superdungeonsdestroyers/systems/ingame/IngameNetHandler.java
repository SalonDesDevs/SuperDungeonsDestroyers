package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import SDD.Common.Room;
import SDD.Server.Content;
import SDD.Server.Environment;
import SDD.Server.Message;
import SDD.Server.Pong;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;

import javax.inject.Inject;

public class IngameNetHandler implements NetworkHandlerSystem.Handler {

    @Override
    public void handle(Message message) {

        switch (message.contentType()) {
            case Content
                    .Environment:
                this.handleEnvironment((Environment) message.content(new Environment()));
                break;
            case Content.Pong:
                this.handlePong((Pong) message.content(new Pong()));
                break;
        }
    }

    private void handlePong(Pong content) {
//        System.err.println("received pong fram ingamenethandler" + content.value());
    }

    @Inject
    MapSwitcher mapSwitcher;

    public void handleEnvironment(Environment environment) {
        Room room = environment.room();
        int kind = room.kind();
        mapSwitcher.scheduleChange(kind);
    }
}
