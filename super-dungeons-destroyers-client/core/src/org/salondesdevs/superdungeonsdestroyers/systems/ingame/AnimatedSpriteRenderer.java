package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.Position;

import javax.inject.Inject;

public class AnimatedSpriteRenderer extends IteratingSystem {
        public AnimatedSpriteRenderer() {
        super(Aspect.all(Animated.class, Position.class));
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch();
    }

    SpriteBatch batch;

    @Inject
    Mapper<Animated> animatedMapper;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    CameraService cameraService;

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
        Position position = positionMapper.get(entity);

        batch.draw(animated.animation.getKeyFrame(stateTime, true), position.x * 16, position.y * 16);
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
