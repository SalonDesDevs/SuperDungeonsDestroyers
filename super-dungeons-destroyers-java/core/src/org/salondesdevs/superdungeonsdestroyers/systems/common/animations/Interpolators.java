package org.salondesdevs.superdungeonsdestroyers.systems.common.animations;

import com.badlogic.gdx.math.MathUtils;

public class Interpolators {

    public static final Interpolator<Float> LINEAR_FLOAT = (a, b, t) -> a * (1-t) + b * t;
    public static final Interpolator<Float> COSINE_FLOAT = (a, b, t) -> LINEAR_FLOAT.interpolate(a, b, 1 - MathUtils.cos(t * MathUtils.PI) / 2.0f);

    public static final Interpolator<Integer> LINEAR_INT = (a, b, t) -> (int) Math.floor(LINEAR_FLOAT.interpolate(a.floatValue(), b.floatValue(), t));
    public static final Interpolator<Integer> COSINE_INT = (a, b, t) -> (int) Math.floor(COSINE_FLOAT.interpolate(a.floatValue(), b.floatValue(), t));
}
