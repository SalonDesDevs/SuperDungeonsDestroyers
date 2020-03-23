package org.salondesdevs.superdungeonsdestroyers.library.events.core;

import net.wytrem.ecs.BaseSystem;
import net.wytrem.ecs.GameState;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Iterator;

@Singleton
public class EventBus extends Service {
    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    private final SubscriberRegistry subscribers = new SubscriberRegistry(this);

    void handleSubscriberException(Throwable cause, SubscriberExceptionContext context) {
        logger.error(message(context), cause);
    }

    public void registerGameState(GameState gameState) {
        for (BaseSystem sys : gameState.systems().allSystems()) {
            register(sys);
        }
    }

    public void unregisterGameState(GameState gameState) {
        for (BaseSystem sys : gameState.systems().allSystems()) {
            unregister(sys);
        }
    }

    private static String message(SubscriberExceptionContext context) {
        Method method = context.getSubscriberMethod();
        return "Exception thrown by subscriber method "
                + method.getName()
                + '('
                + method.getParameterTypes()[0].getName()
                + ')'
                + " on subscriber "
                + context.getSubscriber()
                + " when dispatching event: "
                + context.getEvent();
    }

    /**
     * Registers all subscriber methods on {@code object} to receive events.
     *
     * @param object object whose subscriber methods should be registered.
     */
    public void register(Object object) {
        logger.trace("register(" + "object = " + object + ")");

        subscribers.register(object);
    }

    /**
     * Unregisters all subscriber methods on a registered {@code object}.
     *
     * @param object object whose subscriber methods should be unregistered.
     * @throws IllegalArgumentException if the object was not previously registered.
     */
    public void unregister(Object object) {
        subscribers.unregister(object);
    }

    /**
     * Posts an event to all registered subscribers. This method will return successfully after the
     * event has been posted to all subscribers, and regardless of any exceptions thrown by
     * subscribers.
     *
     * <p>If no subscribers have been subscribed for {@code event}'s class, and {@code event} is not
     * already a {@link DeadEvent}, it will be wrapped in a DeadEvent and reposted.
     *
     * @param event event to post.
     */
    public void post(Event event) {
        Iterator<Subscriber> eventSubscribers = subscribers.getSubscribers(event);

        if (eventSubscribers.hasNext()) {
//            dispatcher.dispatch(event, eventSubscribers);

            while (eventSubscribers.hasNext()) {
                Subscriber next = eventSubscribers.next();

                if (!event.isCancelled() || next.isIgnoreCancelled()) {
//                    logger.trace("{} triggered subscriber {}", event, next);
                    next.dispatchEvent(event);
                }
            }
        } else if (!(event instanceof DeadEvent)) {
            // the event had no subscribers and was not itself a DeadEvent
            post(new DeadEvent(this, event));
        }
    }
}
