package org.salondesdevs.superdungeonsdestroyers.server;

import com.google.inject.AbstractModule;
import net.wytrem.ecs.World;
import net.wytrem.ecs.WorldConfiguration;
import org.salondesdevs.superdungeonsdestroyers.server.states.UniqueState;

public class Server {

    private boolean isRunning;

    public void start() {
        WorldConfiguration worldConfiguration = new WorldConfiguration();
        worldConfiguration.addModule(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Server.class).toInstance(Server.this);
            }
        });

        World world = new World(worldConfiguration);
        world.initialize();

        world.push(UniqueState.class);

        // Main loop
        // TODO: to be improved (fixed timestep mechanic)
        isRunning = true;
        long now, last = System.currentTimeMillis();
        float delta;

        while (isRunning) {
            now = System.currentTimeMillis();
            delta = (now - last) / 1000.0f;

            if (delta < 0.1f) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {

                world.process(delta);
                last = now;
            }

        }

        world.dispose();
    }

    public void stop() {
        isRunning = false;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
