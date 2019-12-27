package org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.*;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.states.IngameState;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;

import javax.inject.Inject;

import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkHandlerSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver.ConnectingToServerNetHandler;

public class MainMenuScreen extends Screen {

    @Inject
    Assets assets;

    @Inject
    World world;

    @Inject
    NetworkSystem networkSystem;

    @Inject
    NetworkHandlerSystem networkHandlerSystem;

    private VisLabel errorLabel;

    @Inject
    protected void addActors() {
        Table table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(assets.titleBackground)));
        table.center();
        table.setFillParent(true);

        VerticalGroup verticalGroup = new VerticalGroup().space(80f);
        table.add(verticalGroup);
        stage.addActor(table);

        VisImage image = new VisImage(assets.title);
        verticalGroup.addActor(image);



        VerticalGroup buttons = new VerticalGroup();
        buttons.space(10f);
        {
            VisTextField textField = new VisTextField("localhost");
            buttons.addActor(textField);

            VisTextButton textButton = new VisTextButton("Se connecter au server");
            buttons.addActor(textButton);

            errorLabel = new VisLabel("");
            buttons.addActor(errorLabel);

            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String text = textField.getText();
                    if (text.length() > 0) {
                        String ip = text;
                        int port = 9000;

                        if (ip.contains(":")) {
                            String[] split = ip.split(":");
                            ip = split[0];

                            port = Integer.parseInt(split[1]);
                        }

                        networkHandlerSystem.setCurrentHandler(ConnectingToServerNetHandler.class);
                        networkSystem.tryConnect(ip, port, MainMenuScreen.this);
                    }
                }
            });
        }

        verticalGroup.addActor(buttons);

    }

    public void connectSuccess() {

        this.world.push(IngameState.class);
    }

    public void setErrorText(String text) {
        errorLabel.setText(text);
    }
}
