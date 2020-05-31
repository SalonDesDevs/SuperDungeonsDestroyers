package org.salondesdevs.superdungeonsdestroyers.library.systems.animations;

import java.util.function.Consumer;

public class Animation<T> {
    private float duration;
    private float advancement;
    private T start, end;
    private Interpolator<T> interpolator;
    private Consumer<T> setter;
    private Runnable onEnd;
    private Runnable onStart;

    private boolean isFinished;

    public Animation(float duration, T start, T end, Interpolator<T> interpolator, Consumer<T> setter) {
        this(duration, start, end, interpolator, setter, null);
    }

    public Animation(float duration, T start, T end, Interpolator<T> interpolator, Consumer<T> setter, Runnable onEnd) {
        this(duration, start, end, interpolator, setter, onEnd, null);
    }

    public Animation(float duration, T start, T end, Interpolator<T> interpolator, Consumer<T> setter, Runnable onEnd, Runnable onStart) {
        this.duration = duration;
        this.start = start;
        this.end = end;
        this.interpolator = interpolator;
        this.setter = setter;
        this.onEnd = onEnd;
        this.onStart = onStart;
    }

    public void tick(float delta) {
        if (!isFinished) {

            this.advancement += delta;
            this.setter.accept(this.interpolator.interpolate(start, end, this.advancement / this.duration));

            if (this.advancement >= duration) {
                if (onEnd != null) {
                    onEnd.run();
                }
                isFinished = true;
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}
