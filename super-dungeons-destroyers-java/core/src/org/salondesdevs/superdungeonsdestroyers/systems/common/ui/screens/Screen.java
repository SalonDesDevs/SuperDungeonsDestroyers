package org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.salondesdevs.superdungeonsdestroyers.library.events.core.EventBus;

import javax.inject.Inject;

public abstract class Screen {

    @Inject
    EventBus eventBus;

    protected Stage stage;

    public Screen() {
        this.stage = new Stage();
    }

    public Stage getStage() {
        return this.stage;
    }
    
    public void dispose() {
        this.stage.dispose();
    }

    public void resized() {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true); 
    }

    public void act(float delta) {
        this.stage.act(delta);
    }

    public void draw() {
        this.stage.draw();
    }

    public void onClosed() {
        eventBus.unregister(this);
    }

    public void onDisplayed() {
        eventBus.register(this);
    }

    public boolean disposeOnClose() {
        return true;
    }
}
