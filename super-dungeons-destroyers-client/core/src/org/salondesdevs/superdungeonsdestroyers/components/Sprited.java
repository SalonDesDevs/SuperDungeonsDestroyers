package org.salondesdevs.superdungeonsdestroyers.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.wytrem.ecs.*;

public class Sprited implements Component {
    public TextureRegion textureRegion;

    public Sprited(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public enum Sprites {
        IRON_CHEST_CLOSED(224, 176),
        IRON_CHEST_EMPTY(224, 192),
        IRON_CHEST_FULL(224, 208),
        GOLDEN_CHEST_CLOSED(240, 176),
        GOLDEN_CHEST_EMPTY(240, 192),
        GOLDEN_CHEST_FULL(240, 208),
        MAGE(144, 224);

        public final int x, y;

        Sprites(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public TextureRegion toTextureRegion(Texture texture) {
            return new TextureRegion(texture, x, y, 16, 16);
        }
    }
}
