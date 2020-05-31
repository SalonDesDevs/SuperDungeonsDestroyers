package org.salondesdevs.superdungeonsdestroyers.library.systems.animations;

/**
 * Some standard interpolator.
 */
public class Interpolators {

    public static final Interpolator<Float> LINEAR_FLOAT = (a, b, t) -> a * (1-t) + b * t;
    public static final Interpolator<Float> COSINE_FLOAT = (a, b, t) -> LINEAR_FLOAT.interpolate(a, b, (float) (1 - Math.cos(t * Math.PI) / 2.0f));

    public static final Interpolator<Integer> LINEAR_INT = (a, b, t) -> (int) Math.floor(LINEAR_FLOAT.interpolate(a.floatValue(), b.floatValue(), t));
    public static final Interpolator<Integer> COSINE_INT = (a, b, t) -> (int) Math.floor(COSINE_FLOAT.interpolate(a.floatValue(), b.floatValue(), t));
}
