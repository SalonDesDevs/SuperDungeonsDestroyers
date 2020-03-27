package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Injector;
import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.World;
import org.salondesdevs.superdungeonsdestroyers.components.Me;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.ChatScreen;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HudRenderer extends IteratingSystem {

    SpriteBatch batch;
    BitmapFont font;

    @Inject
    Injector injector;

    public HudRenderer() {
        super(Aspect.all(Me.class, Position.class));
    }

    @Override
    public void initialize() {
        batch = new GridSpriteBatch();
        font = new BitmapFont();
        this.chatScreen = injector.getInstance(ChatScreen.class);
    }

    @Override
    public void begin() {
        batch.begin();
    }

    @Inject
    Mapper<Position> positionMapper;

    ChatScreen chatScreen;

    @Inject
    UiSystem uiSystem;

    @Inject
    World world;

    @Override
    public void process(int entity) {
        if (uiSystem.getCurrentScreen() == null || !(uiSystem.getCurrentScreen() instanceof ChatScreen)) {
            chatScreen.getStage().act(world.getDelta());
            chatScreen.getStage().draw();
        }

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
