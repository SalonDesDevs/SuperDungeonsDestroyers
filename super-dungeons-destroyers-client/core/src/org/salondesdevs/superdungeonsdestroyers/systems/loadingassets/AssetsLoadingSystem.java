package org.salondesdevs.superdungeonsdestroyers.systems.loadingassets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.states.MainMenuState;
import org.salondesdevs.superdungeonsdestroyers.systems.Assets;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.soap.SAAJResult;
import java.lang.reflect.Field;

@Singleton
public class AssetsLoadingSystem extends BaseSystem {

    AssetManager assetManager;

    @Inject
    Assets assets;

    SpriteBatch batch;
    BitmapFont font;

    @Override
    public void initialize() {
        this.assetManager = new AssetManager();
        this.assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        registerAssets();
        batch = new SpriteBatch();
        font = new BitmapFont();
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
        batch.begin();
    }

    @Override
    public void process() {
        if (this.assetManager.update()) {
            fillAssets();
            this.world.push(MainMenuState.class);
        }
        else {
            // TODO: make it much more pretty
            font.draw(batch, " loading assets, progress = " + this.assetManager.getProgress(), 20, 20);
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
