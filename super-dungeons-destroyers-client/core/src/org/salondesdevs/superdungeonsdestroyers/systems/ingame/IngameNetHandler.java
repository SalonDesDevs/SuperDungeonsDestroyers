package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import SDD.Common.Entity;
import SDD.Common.Level;
import SDD.Server.Content;
import SDD.Server.Environment;
import SDD.Server.Message;
import SDD.Server.Pong;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class IngameNetHandler implements NetworkHandlerSystem.Handler {

    private static final Logger logger = LoggerFactory.getLogger( IngameNetHandler.class );

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
        logger.trace("");
        Level level = environment.level();
        int kind = level.kind();
        mapSwitcher.scheduleChange(kind);

        for (int i = 0; i < environment.entitiesLength(); i++) {
            Entity entity = environment.entities(i);
        }
    }
}
