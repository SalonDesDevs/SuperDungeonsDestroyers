package org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.states.ConnectingToServer;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;

import javax.inject.Inject;

public class MainMenuScreen extends Screen {

    @Inject
    Assets assets;

    @Inject
    World world;

    @Inject
    protected void addActors() {
        Table table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(assets.titleBackground)));
        table.center();
        table.setFillParent(true);
        stage.addActor(table);

        Image image = new Image(assets.title);
        table.add(image);

        TextButton textButton = new TextButton("Se connecter au server", assets.skin);
        table.add(textButton);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                world.push(ConnectingToServer.class);
            }
        });
    }
}
