package org.salondesdevs.superdungeonsdestroyers.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.wytrem.ecs.Component;

import java.util.Map;

public class AnimationsRegistry implements Component {
    private final Map<ActionState, Animation<TextureRegion>> registry;

    public AnimationsRegistry(Map<ActionState, Animation<TextureRegion>> registry) {
        this.registry = registry;
    }

    public Animation<TextureRegion> get(ActionState key) {
        return registry.get(key);
    }
}
