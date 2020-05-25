package org.salondesdevs.superdungeonsdestroyers.library.components.watched;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.wytrem.ecs.Component;
import org.salondesdevs.superdungeonsdestroyers.library.components.*;

import java.util.function.Supplier;

/**
 * A class used to register components which are automatically updated by the server's synchronizer.
 *
 * Just add a line into the static block and you're done.
 */
public class AutoWatchedComponents {

    private static final Byte2ObjectMap<Supplier<? extends AutoWatched>> suppliers = new Byte2ObjectOpenHashMap<>();
    private static final Byte2ObjectMap<Class<? extends AutoWatched>> classesById = new Byte2ObjectOpenHashMap<>();
    private static final Object2ByteMap<Class<? extends AutoWatched>> classes = new Object2ByteOpenHashMap<>();

    static {
        int id = 0;
        register(id++, Name::new, Name.class);
        register(id++, Size::new, Size.class);
        register(id++, null, Facing.class);
        register(id++, Health::new, Health.class);
        register(id++, MaxHealth::new, MaxHealth.class);
        register(id++, RemainingSteps::new, RemainingSteps.class);
        register(id++, Speed::new, Speed.class);
        register(id++, null, Role.class);
    }

    private static <P extends AutoWatched> void register(int id, Supplier<P> componentSupplier, Class<P> clazz) {
        if (id > 126) {
           return;
        }
        suppliers.put((byte) id, componentSupplier);
        classesById.put((byte) id, clazz);
        classes.put(clazz, (byte) id);
    }

    public static byte getId(AutoWatched component) {
        if (classes.containsKey(component.getClass())) {
            return classes.getByte(component.getClass());
        }
        else {
            throw new IllegalArgumentException("Unregistered serializable component " + component.getClass());
        }
    }

    public static boolean contains(Class<? extends Component> clazz) {
        return classes.containsKey(clazz);
    }

    public static AutoWatched createFromId(byte id) {
        return suppliers.get(id).get();
    }
    
    public static Class<? extends AutoWatched> getClassById(byte id) {
        return classesById.get(id);
    }
}
