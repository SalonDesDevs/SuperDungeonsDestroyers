package org.salondesdevs.superdungeonsdestroyers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.google.inject.AbstractModule;
import net.wytrem.ecs.World;
import net.wytrem.ecs.WorldConfiguration;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Version;
import org.salondesdevs.superdungeonsdestroyers.states.LoadingAssets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class SuperDungeonsDestroyersClient extends ApplicationAdapter {


    private static final Logger logger = LoggerFactory.getLogger(SuperDungeonsDestroyersClient.class);

    World world;

    private Set<Runnable> resizeListeners = new HashSet<Runnable>();

    @Override
    public void create() {
        logger.info("Starting SDDClient v{}", Version.VERSION);
        logger.info("Using libgdx v{}", com.badlogic.gdx.Version.VERSION);
        WorldConfiguration worldConfiguration = new WorldConfiguration();
        worldConfiguration.addModule(new AbstractModule() {
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

    public void removeResizeListener(Runnable run) {
        this.resizeListeners.remove(run);
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
