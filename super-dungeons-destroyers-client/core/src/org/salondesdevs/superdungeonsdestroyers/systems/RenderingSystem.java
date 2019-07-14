package org.salondesdevs.superdungeonsdestroyers.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.states.ConnectingToServer;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RenderingSystem extends BaseSystem {
    @Inject
    Assets assets;

    SpriteBatch batch;

    BitmapFont font;

    float remaining = 6.0f;

    @Override
    public void initialize() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void begin() {
        batch.begin();
    }

    @Override
    public void process() {
        remaining -= world.getDelta();

        if (remaining < 0) {
            world.push(ConnectingToServer.class);
        } else {
            // TODO: create a GuiSystem
            font.draw(batch, "Ceci est le menu principal. " +
                    "Non, pour l'instant on ne peut pas cliquer. " +
                    "Oui, pour l'instant c'est moche. \n" +
                    "Connexion au serveur dans " + Math.ceil(remaining), Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() - 30, 200, Align.center, true);


            batch.draw(assets.title, (Gdx.graphics.getWidth() - assets.title.getWidth()) / 2, (Gdx.graphics.getHeight() - assets.title.getHeight()) / 2);
        }
    }

    @Override
    public void end() {
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
