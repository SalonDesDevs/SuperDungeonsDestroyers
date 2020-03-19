package org.salondesdevs.superdungeonsdestroyers.systems.ingame.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.salondesdevs.superdungeonsdestroyers.components.Offset;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Size;

public class GridSpriteBatch extends SpriteBatch {

    public static final float GRID_SIZE = 32.0f;
    public static final float UNIT_SCALE = GRID_SIZE / 16.0f;

    public void draw(TextureRegion region, Position position) {
        this.draw(region, position.x * GRID_SIZE, position.y * GRID_SIZE, region.getRegionWidth() * UNIT_SCALE, region.getRegionHeight() * UNIT_SCALE);
    }

    public void draw(TextureRegion region, Position position, Offset offset) {
        this.draw(region, (position.x + offset.x) * GRID_SIZE, (position.y + offset.y) * GRID_SIZE, region.getRegionWidth() * UNIT_SCALE, region.getRegionHeight() * UNIT_SCALE);
    }

    public void draw(TextureRegion region, Position position, Offset offset, Size size) {
        this.draw(region, (position.x + offset.x) * GRID_SIZE, (position.y + offset.y) * GRID_SIZE, size.getWidth() * GRID_SIZE, size.getHeight() * GRID_SIZE);
    }

    public void draw(TextureRegion region, Position position, Size size) {
        this.draw(region, position.x * GRID_SIZE, position.y * GRID_SIZE, size.getWidth() * GRID_SIZE, size.getHeight() * GRID_SIZE);
    }

    public static float convert(float grid) {
        return grid * GRID_SIZE;
    }

    public static float toGridCoordsX(Position position) {
        return position.x * GRID_SIZE;
    }

    public static float toGridCoordsY(Position position) {
        return position.y * GRID_SIZE;
    }

    public static float toGridCoordsX(Position position, Offset offset) {
        return (position.x + offset.x) * GRID_SIZE;
    }

    public static float toGridCoordsY(Position position, Offset offset) {
        return (position.y + offset.y) * GRID_SIZE;
    }
}
