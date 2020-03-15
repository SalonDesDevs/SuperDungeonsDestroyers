package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Name;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Size;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;

@Singleton
public class AnimatedSpriteRenderer extends IteratingSystem {
    public AnimatedSpriteRenderer() {
        super(Aspect.all(Animated.class, Position.class));
    }

    @Override
    public void initialize() {
        batch = new GridSpriteBatch();
        font = new BitmapFont();
    }

    GridSpriteBatch batch;
        BitmapFont font;

    @Inject
    Mapper<Animated> animatedMapper;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    Mapper<Size> sizeMapper;

    @Inject
    CameraSystem cameraService;

    float stateTime = 0.0f;

    @Override
    public void begin() {
        stateTime += world.getDelta();
        batch.setProjectionMatrix(cameraService.camera.combined);
        batch.begin();
    }

    @Inject
    Mapper<Name> nameMapper;

    @Override
    public void process(int entity) {
        Animated animated = animatedMapper.get(entity);
        Position position = positionMapper.get(entity);

        TextureRegion region = animated.animation.getKeyFrame(stateTime, true);

        if (offsetMapper.has(entity)) {
            Offset offset = offsetMapper.get(entity);

            if (sizeMapper.has(entity)) {
                Size size = sizeMapper.get(entity);
                batch.draw(region, position, offset, size);
            }
            else {
                batch.draw(region, position, offset);
            }
        }
        else if (sizeMapper.has(entity)){
            Size size = sizeMapper.get(entity);
            batch.draw(region, position, size);
        }
        else {
            batch.draw(region, position);
        }

//        if (nameMapper.has(entity)) {
//            font.draw(batch, nameMapper.get(entity).getValue(), position.x * 13 + offset.x, position.y * 16 + offset.y);
//        }
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
