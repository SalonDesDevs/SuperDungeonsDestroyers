package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.TilePosition;
import org.salondesdevs.superdungeonsdestroyers.components.Sprited;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpriteRenderer extends IteratingSystem {
    public SpriteRenderer() {
        super(Aspect.all(Sprited.class, TilePosition.class));
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch();
    }

    SpriteBatch batch;

    @Inject
    Mapper<Sprited> spritedMapper;

    @Inject
    Mapper<TilePosition> positionMapper;

    @Inject
    CameraSystem cameraService;

    @Override
    public void begin() {
        batch.setProjectionMatrix(cameraService.camera.combined);
        batch.begin();
    }

    @Override
    public void process(int entity) {
        Sprited sprited = spritedMapper.get(entity);
        TilePosition tilePosition = positionMapper.get(entity);

        batch.draw(sprited.textureRegion, tilePosition.x * 16, tilePosition.y * 16);
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
