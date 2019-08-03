package org.salondesdevs.superdungeonsdestroyers.systems.common.animations;

@FunctionalInterface
public interface Interpolator<T> {
    T interpolate(T a, T b, float t);
}
