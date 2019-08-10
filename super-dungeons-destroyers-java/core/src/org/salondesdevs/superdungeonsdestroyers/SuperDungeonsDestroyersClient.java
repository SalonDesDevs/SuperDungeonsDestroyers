package org.salondesdevs.superdungeonsdestroyers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.google.inject.AbstractModule;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.states.LoadingAssets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class SuperDungeonsDestroyersClient extends ApplicationAdapter {

    public static final String VERSION = "1.0";

    private static final Logger logger = LoggerFactory.getLogger(SuperDungeonsDestroyersClient.class);

    World world;

    private Set<Runnable> resizeListeners = new HashSet<Runnable>();

    @Override
    public void create() {
        logger.info("Starting SDDClient v{}", VERSION);
        WorldConfiguration worldConfiguration = new WorldConfiguration();
        worldConfiguration.getExtraModules().add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(SuperDungeonsDestroyersClient.class).toInstance(SuperDungeonsDestroyersClient.this);
            }
        });

        world = new World(worldConfiguration);
        world.initialize();


        world.push(LoadingAssets.class);
    }

    public void addResizeListener(Runnable run) {
        this.resizeListeners.add(run);
    }

    @Override
    public void resize(int width, int height) {
        this.resizeListeners.forEach(Runnable::run);
    }

    @Override
    public void render() {
        world.process(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}