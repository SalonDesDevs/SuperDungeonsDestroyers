package org.salondesdevs.superdungeonsdestroyers.library.components;

import net.wytrem.ecs.Component;

public enum Facing implements Component {
    NORTH(0, 1),
    SOUTH(0, -1),
    WEST(-1, 0),
    EAST(1, 0);

    public final int x, y;

    Facing(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Facing getClosest(float x, float y) {
        Facing result = null;
        float maxDot = Float.MIN_VALUE;
        float dot;

        for (Facing dir : values()) {
            dot = dir.x * x + dir.y * y;
            if (dot > maxDot) {
                maxDot = dot;
                result = dir;
            }
        }

        return result;
    }
}
