package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnimatedSpriteRenderer extends IteratingSystem {
        public AnimatedSpriteRenderer() {
        super(Aspect.all(Animated.class, Position.class, Offset.class));
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
        Position position = positionMapper.get(entity);
        Offset offset = offsetMapper.get(entity);

        batch.draw(animated.animation.getKeyFrame(stateTime, true), position.x * 16 + offset.x, position.y * 16 + offset.y);
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
