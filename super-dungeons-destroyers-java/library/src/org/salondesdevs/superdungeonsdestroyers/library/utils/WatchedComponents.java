package org.salondesdevs.superdungeonsdestroyers.library.utils;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.wytrem.ecs.Component;
import org.salondesdevs.superdungeonsdestroyers.library.components.*;

import java.util.function.Supplier;

public class WatchedComponents {


    private static final Byte2ObjectMap<Supplier<? extends Component>> suppliers = new Byte2ObjectOpenHashMap<>();
    private static final Byte2ObjectMap<Class<? extends Component>> classesById = new Byte2ObjectOpenHashMap<>();
    private static final Object2ByteMap<Class<? extends Component>> classes = new Object2ByteOpenHashMap<>();

    static {
        // Both sides
        int id = 0;
        register(id++, Name::new, Name.class);
        register(id++, Size::new, Size.class);
        register(id++, null, Facing.class);
        register(id++, Health::new, Health.class);
        register(id++, MaxHealth::new, MaxHealth.class);
        register(id++, RemainingSteps::new, RemainingSteps.class);
    }

    private static <P extends Component> void register(int id, Supplier<P> componentSupplier, Class<P> clazz) {
        suppliers.put((byte) id, componentSupplier);
        classesById.put((byte) id, clazz);
        classes.put(clazz, (byte) id);
    }

    public static byte getId(Component component) {
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

    public static Component createFromId(byte id) {
        return suppliers.get(id).get();
    }
    
    public static Class<? extends Component> getClassById(byte id) {
        return classesById.get(id);
    }
}
