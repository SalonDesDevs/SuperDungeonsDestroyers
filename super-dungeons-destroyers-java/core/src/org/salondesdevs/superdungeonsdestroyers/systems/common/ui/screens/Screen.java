package org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Screen {
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
}
