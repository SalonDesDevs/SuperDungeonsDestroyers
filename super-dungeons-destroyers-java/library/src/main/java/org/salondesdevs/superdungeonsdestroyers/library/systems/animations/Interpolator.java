package org.salondesdevs.superdungeonsdestroyers.library.systems.animations;

@FunctionalInterface
public interface Interpolator<T> {
    /**
     * Interpolates between two values according to the given float coefficient.
     */
    T interpolate(T a, T b, float t);
}
