package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.components.TilePosition;

import javax.inject.Inject;

public class AnimatedSpriteRenderer extends IteratingSystem {
        public AnimatedSpriteRenderer() {
        super(Aspect.all(Animated.class, TilePosition.class, Offset.class));
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch();
    }

    SpriteBatch batch;

    @Inject
    Mapper<Animated> animatedMapper;

    @Inject
    Mapper<TilePosition> positionMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    CameraSystem cameraService;

    float stateTime = 0.0f;

    @Override
    public void begin() {
        stateTime += world.getDelta();
        batch.setProjectionMatrix(cameraService.camera.combined);
        batch.begin();
    }

    @Override
    public void process(int entity) {
        Animated animated = animatedMapper.get(entity);
        TilePosition tilePosition = positionMapper.get(entity);
        Offset offset = offsetMapper.get(entity);

        batch.draw(animated.animation.getKeyFrame(stateTime, true), tilePosition.x * 16 + offset.x, tilePosition.y * 16 + offset.y);
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
