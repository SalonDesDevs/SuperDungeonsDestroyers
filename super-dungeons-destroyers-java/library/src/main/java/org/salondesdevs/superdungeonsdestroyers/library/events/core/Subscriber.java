/*
 * Copyright (C) 2014 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.salondesdevs.superdungeonsdestroyers.library.events.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.eventbus.AllowConcurrentEvents;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A subscriber method on a specific object, plus the executor that should be used for dispatching
 * events to it.
 *
 * <p>Two subscribers are equivalent when they refer to the same method on the same object (not
 * class). This property is used to ensure that no subscriber method is registered more than once.
 *
 * @author Colin Decker
 */
class Subscriber {

    /**
     * Creates a {@code Subscriber} for {@code method} on {@code listener}.
     */
    static Subscriber create(EventBus bus, Object listener, Method method, EventHandler.Priority priority, boolean ignoreCancelled) {
        return isDeclaredThreadSafe(method)
                ? new Subscriber(bus, listener, method, priority, ignoreCancelled)
                : new SynchronizedSubscriber(bus, listener, method, priority, ignoreCancelled);
    }

    static Comparator<Subscriber> COMPARATOR = new Comparator<Subscriber>() {
        @Override
        public int compare(Subscriber o1, Subscriber o2) {
            return o1.getPriority().ordinal() - o2.getPriority().ordinal();
        }
    };

    /**
     * The event bus this subscriber belongs to.
     */
    private EventBus bus;

    /**
     * The object with the subscriber method.
     */
    final Object target;

    /**
     * Subscriber method.
     */
    private final Method method;

    private final EventHandler.Priority priority;
    private final boolean ignoreCancelled;

    private Subscriber(EventBus bus, Object target, Method method, EventHandler.Priority priority, boolean ignoreCancelled) {
        this.bus = bus;
        this.target = checkNotNull(target);
        this.method = method;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
        method.setAccessible(true);
    }

    public EventHandler.Priority getPriority() {
        return priority;
    }

    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }

    /**
     * Dispatches {@code event} to this subscriber using the proper executor.
     */
    final void dispatchEvent(final Object event) {
        try {
            invokeSubscriberMethod(event);
        } catch (InvocationTargetException e) {
            bus.handleSubscriberException(e.getCause(), context(event));
        }
    }

    /**
     * Invokes the subscriber method. This method can be overridden to make the invocation
     * synchronized.
     */
    @VisibleForTesting
    void invokeSubscriberMethod(Object event) throws InvocationTargetException {
        try {
            method.invoke(target, checkNotNull(event));
        } catch (IllegalArgumentException e) {
            throw new Error("Method rejected target/argument: " + event, e);
        } catch (IllegalAccessException e) {
            throw new Error("Method became inaccessible: " + event, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            throw e;
        }
    }

    /**
     * Gets the context for the given event.
     */
    private SubscriberExceptionContext context(Object event) {
        return new SubscriberExceptionContext(bus, event, target, method);
    }

    @Override
    public final int hashCode() {
        return (31 + method.hashCode()) * 31 + System.identityHashCode(target);
    }

    @Override
    public final boolean equals(@Nullable Object obj) {
        if (obj instanceof Subscriber) {
            Subscriber that = (Subscriber) obj;
            // Use == so that different equal instances will still receive events.
            // We only guard against the case that the same object is registered
            // multiple times
            return target == that.target && method.equals(that.method);
        }
        return false;
    }

    @Override
    public String toString() {
//        return "Subscriber[method=" + this.method.getName() + "(" + this.method.getParameterTypes()[0].getSimpleName() + "), class=" + this.method.getDeclaringClass() + "]";
        return this.method.getDeclaringClass().getSimpleName() + "::" + this.method.getName();
    }

    /**
     * Checks whether {@code method} is thread-safe, as indicated by the presence of the {@link
     * AllowConcurrentEvents} annotation.
     */
    private static boolean isDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }

    /**
     * Subscriber that synchronizes invocations of a method to ensure that only one thread may enter
     * the method at a time.
     */
    @VisibleForTesting
    static final class SynchronizedSubscriber extends Subscriber {

        private SynchronizedSubscriber(EventBus bus, Object target, Method method, EventHandler.Priority priority, boolean ignoreCancelled) {
            super(bus, target, method, priority, ignoreCancelled);
        }

        @Override
        void invokeSubscriberMethod(Object event) throws InvocationTargetException {
            synchronized (this) {
                super.invokeSubscriberMethod(event);
            }
        }
    }
}
