package org.salondesdevs.superdungeonsdestroyers.systems.loadingassets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.states.MainMenuState;
import org.salondesdevs.superdungeonsdestroyers.systems.Assets;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;

@Singleton
/**
 * Detects {@link Assets} fields to be loaded and run an {@link AssetManager}.
 */
public class AssetsLoadingSystem extends BaseSystem {

    private AssetManager assetManager;

    @Inject
    Assets assets;

    SpriteBatch batch;
    BitmapFont font;
    Texture splash;

    @Override
    public void initialize() {
        this.assetManager = new AssetManager();
        this.assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        registerAssets();
        batch = new SpriteBatch();
        splash = new Texture("splash.png");
        font = new BitmapFont();
        font.setColor(Color.BLACK);
    }

    private void registerAssets() {
        for (Field field : Assets.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Assets.Asset.class)) {
                Assets.Asset assetAnnotation = field.getAnnotation(Assets.Asset.class);

                Class<?> type =  assetAnnotation.type().equals(Assets.UseFieldType.class) ? field.getType() : assetAnnotation.type();
//                System.out.println("Registering " + assetAnnotation.path() + " with type " + type);
                this.assetManager.load(assetAnnotation.path(), type);
            }
        }
    }


    private void fillAssets() {
        for (Field field : Assets.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Assets.Asset.class)) {
                Assets.Asset assetAnnotation = field.getAnnotation(Assets.Asset.class);
                field.setAccessible(true);

                try {
//                   System.out.println("Setting " + assetAnnotation.path() + " to " + this.assetManager.get(assetAnnotation.path()));
                    field.set(assets, this.assetManager.get(assetAnnotation.path()));
                } catch (IllegalAccessException e) {
                    System.err.println("Could not load asset called " + assetAnnotation.path() + " with type " + assetAnnotation.type());
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void begin() {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
    }

    @Override
    public void process() {
        if (this.assetManager.update()) {
            fillAssets();
            this.world.push(MainMenuState.class);
        }
        else {
            batch.draw(splash, (Gdx.graphics.getWidth() - splash.getWidth()) / 2, (Gdx.graphics.getHeight() - splash.getHeight()) / 2);
            font.draw(batch, "Loading assets: " + (this.assetManager.getProgress() * 100) + "%", 0, 20);
        }
    }

    @Override
    public void end() {
        batch.end();
    }

    @Override
    public void dispose() {
        this.assetManager.dispose();
    }
}
