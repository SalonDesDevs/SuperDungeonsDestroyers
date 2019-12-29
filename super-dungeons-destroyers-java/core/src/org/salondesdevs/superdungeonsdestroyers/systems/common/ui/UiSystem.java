package org.salondesdevs.superdungeonsdestroyers.systems.common.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.google.inject.Injector;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.usl.USL;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.SuperDungeonsDestroyersClient;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.Screen;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.IngameInputSystem;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;

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

    public void displayScreen(Class<? extends Screen> screenClass) {
        if (this.currentScreen != null) {
            this.currentScreen.dispose();
        }

        if (screenClass != null) {
            this.currentScreen = injector.getInstance(screenClass);
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
