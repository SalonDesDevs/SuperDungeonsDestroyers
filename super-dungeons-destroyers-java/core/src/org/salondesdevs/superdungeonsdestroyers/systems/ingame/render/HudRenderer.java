package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Injector;
import net.wytrem.ecs.Aspect;
import net.wytrem.ecs.IteratingSystem;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.World;
import org.salondesdevs.superdungeonsdestroyers.components.Me;
import org.salondesdevs.superdungeonsdestroyers.library.components.Health;
import org.salondesdevs.superdungeonsdestroyers.library.components.MaxHealth;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.RemainingSteps;
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

    @Inject
    Mapper<RemainingSteps> remainingStepsMapper;

    @Inject
    Mapper<Health> healthMapper;

    @Inject
    Mapper<MaxHealth> maxHealthMapper;

    @Inject
    Mapper<Position> positionMapper;

    // do not mark @Inject, this needs to be created only on system init
    ChatScreen chatScreen;

    @Inject
    UiSystem uiSystem;

    @Inject
    World world;

    public HudRenderer() {
        super(Aspect.all(Me.class));
    }

    @Override
    public void initialize() {
        batch = new GridSpriteBatch();
        font = new BitmapFont();
        this.chatScreen = injector.getInstance(ChatScreen.class);
    }


    @Override
    public void process(int entity) {
        if (uiSystem.getCurrentScreen() == null || !(uiSystem.getCurrentScreen() instanceof ChatScreen)) {
            chatScreen.getStage().act(world.getDelta());
            chatScreen.getStage().draw();
        }

        {
            final int PADDING = 20;
            int x = 10;
            int y = 0;
            batch.begin();

            y += PADDING;
            font.draw(batch, "Entity id: " + entity, x, y);

            if (positionMapper.has(entity)) {
                y += PADDING;
                Position position = positionMapper.get(entity);
                font.draw(batch, "Pos: " + position.x + ", " + position.y, x, y);
            }

            if (remainingStepsMapper.has(entity)) {
                y += PADDING;
                RemainingSteps remainingSteps = remainingStepsMapper.get(entity);
                font.draw(batch, "Steps: " + remainingSteps.get(), x, y);
            }

            if (healthMapper.has(entity)) {
                y += PADDING;
                Health health = healthMapper.get(entity);
                String str = "Health: " + health.get();

                if (maxHealthMapper.has(entity)) {
                    str += "/" + maxHealthMapper.get(entity).get();
                }

                font.draw(batch, str, x, y);
            }

            batch.end();
        }
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}
