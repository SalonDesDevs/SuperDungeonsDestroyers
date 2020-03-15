package org.salondesdevs.superdungeonsdestroyers.library.systems.animations;

@FunctionalInterface
public interface Interpolator<T> {
    T interpolate(T a, T b, float t);
}
