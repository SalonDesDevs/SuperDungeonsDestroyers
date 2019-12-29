package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Name;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;

import javax.inject.Inject;

public class NameRenderer extends IteratingSystem {
    public NameRenderer() {
        super(Aspect.all(Name.class, Position.class));
    }

    @Inject
    Mapper<Name> nameMapper;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Mapper<Offset> offsetMapper;

    @Inject
    CameraSystem cameraService;

    SpriteBatch batch;
    BitmapFont font;

    @Override
    public void initialize() {
        batch = new GridSpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void begin() {
        batch.setProjectionMatrix(cameraService.camera.combined);
        batch.begin();
    }

    @Override
    public void process(int entity) {
        Name name = nameMapper.get(entity);
        Position position = positionMapper.get(entity);
        float x = GridSpriteBatch.toGridCoordsX(position);
        float y = GridSpriteBatch.toGridCoordsY(position);

        if (offsetMapper.has(entity)) {
            Offset offset = offsetMapper.get(entity);
            x = GridSpriteBatch.toGridCoordsX(position, offset);
            y = GridSpriteBatch.toGridCoordsY(position, offset);
        }

        font.draw(batch, name.getValue(), x, y);
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
