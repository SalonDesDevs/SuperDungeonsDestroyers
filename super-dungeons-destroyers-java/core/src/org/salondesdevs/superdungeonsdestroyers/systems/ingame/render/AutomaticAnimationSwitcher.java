package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.components.ActionState;
import org.salondesdevs.superdungeonsdestroyers.components.Animated;
import org.salondesdevs.superdungeonsdestroyers.components.AnimationsRegistry;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Listens for {@link ActionState} changes and sets the value of {@link Animated} according to the {@link AnimationsRegistry}.
 */
@Singleton
public class AutomaticAnimationSwitcher extends Service {

    @Inject
    Mapper<AnimationsRegistry> animationsRegistryMapper;

    @Inject
    Mapper<Animated> animatedMapper;

    @Inject
    Mapper<ActionState> actionStateMapper;

    @Override
    public void initialize() {
        actionStateMapper.addListener(new Mapper.ChangeListener<ActionState>() {
            @Override
            public void onSet(int entity, ActionState oldValue, ActionState newValue) {
                updateAnimation(entity);
            }

            @Override
            public void onUnset(int entity, ActionState oldValue) {

            }
        });

        animationsRegistryMapper.addListener(new Mapper.ChangeListener<AnimationsRegistry>() {
            @Override
            public void onSet(int entity, AnimationsRegistry oldValue, AnimationsRegistry newValue) {
                updateAnimation(entity);
            }

            @Override
            public void onUnset(int entity, AnimationsRegistry oldValue) {

            }
        });
    }

    private void updateAnimation(int entity) {
        if (animationsRegistryMapper.has(entity)) {

            Animation<TextureRegion> animation = animationsRegistryMapper.get(entity).get(actionStateMapper.get(entity));

            if (animatedMapper.has(entity)) {
                animatedMapper.get(entity).setAnimation(animation);
            }
            else {
                animatedMapper.set(entity, new Animated(animation));
            }
        }
    }
}
