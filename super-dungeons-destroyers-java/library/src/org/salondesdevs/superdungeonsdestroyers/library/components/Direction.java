package org.salondesdevs.superdungeonsdestroyers.library.components;

public enum Direction {
    NORTH(0, 1),
    SOUTH(0, -1),
    WEST(-1, 0),
    EAST(1, 0);

    public final int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction getClosest(float x, float y) {
        Direction result = null;
        float maxDot = Float.MIN_VALUE;
        float dot;

        for (Direction dir : values()) {
            dot = dir.x * x + dir.y * y;
            if (dot > maxDot) {
                maxDot = dot;
                result = dir;
            }
        }

        return result;
    }
}
