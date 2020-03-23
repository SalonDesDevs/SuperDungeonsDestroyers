package org.salondesdevs.superdungeonsdestroyers.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.wytrem.ecs.Component;

public class Animated implements Component {
    private Animation<TextureRegion> animation;

    public Animated(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }
}
