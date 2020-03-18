package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;
import org.salondesdevs.superdungeonsdestroyers.components.Me;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;

import javax.inject.Inject;

public class HudRenderer extends IteratingSystem {

    SpriteBatch batch;
    BitmapFont font;

    public HudRenderer() {
        super(Aspect.all(Me.class, Position.class));
    }

    @Override
    public void initialize() {
        batch = new GridSpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void begin() {
        batch.begin();
    }

    @Inject
    Mapper<Position> positionMapper;

    @Override
    public void process(int entity) {
        Position position = positionMapper.get(entity);
        font.draw(batch, "Pos: " + position.x + ", " + position.y, 20,20);
    }

    @Override
    public void end() {
        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}
