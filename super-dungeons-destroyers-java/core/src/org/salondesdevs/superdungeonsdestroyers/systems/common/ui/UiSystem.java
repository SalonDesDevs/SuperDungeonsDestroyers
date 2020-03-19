package org.salondesdevs.superdungeonsdestroyers.systems.common.ui;

import com.badlogic.gdx.Gdx;
import com.google.inject.Injector;
import com.kotcrab.vis.ui.VisUI;
import net.wytrem.ecs.BaseSystem;
import org.salondesdevs.superdungeonsdestroyers.SuperDungeonsDestroyersClient;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.Screen;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.IngameInputSystem;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UiSystem extends BaseSystem {

    @Inject
    IngameInputSystem ingameInputSystem;

    @Inject
    SuperDungeonsDestroyersClient superDungeonsDestroyersClient;

    private Screen currentScreen;

    @Inject
    Injector injector;

    @Inject
    Assets assets;

    @Override
    public void initialize() {
        VisUI.load(assets.skin);
        superDungeonsDestroyersClient.addResizeListener(this::resized);
    }

    private void resized() {
        if (currentScreen != null) {
            this.currentScreen.resized();
        }
    }

    @Override
    public void process() {
        if (currentScreen != null) {
            currentScreen.act(world.getDelta());
            currentScreen.draw();
        }
    }

    public void closeScreen() {
        displayScreen(null);
    }

    public void displayScreen(Class<? extends Screen> screenClass) {
        if (this.currentScreen != null) {
            this.currentScreen.onClosed();
            this.currentScreen.dispose();
        }

        if (screenClass != null) {
            this.currentScreen = injector.getInstance(screenClass);
            this.currentScreen.onDisplayed();
            Gdx.input.setInputProcessor(this.currentScreen.getStage());
        }
        else {
            this.currentScreen = null;
            Gdx.input.setInputProcessor(ingameInputSystem);
        }
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }
}
