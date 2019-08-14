package org.salondesdevs.superdungeonsdestroyers.library.packets;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.EntityMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.DisconnectReason;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.Welcome;

import java.util.function.Supplier;

public abstract class Packet {

    public abstract void read(ByteBuf in);

    public abstract void write(ByteBuf out);

    private static final Byte2ObjectMap<Supplier<? extends Packet>> suppliers = new Byte2ObjectOpenHashMap<>();
    private static final Object2ByteMap<Class<? extends Packet>> classes = new Object2ByteOpenHashMap<>();

    static {
        // Both sides
        register(0, KeepAlive::new, KeepAlive.class);


        // From client
        register(10, VersionCheck::new, VersionCheck.class);
        register(11, EntityMove::new, EntityMove.class);


        // From server
        register(51, DisconnectReason::new, DisconnectReason.class);
        register(52, Welcome::new, Welcome.class);

    }

    private static <P extends Packet> void register(int id, Supplier<P> packetSupplier, Class<P> clazz) {
        suppliers.put((byte) id, packetSupplier);
        classes.put(clazz, (byte) id);
    }

    public static byte getId(Packet packet) {
        if (classes.containsKey(packet.getClass())) {
            return classes.getByte(packet.getClass());
        }
        else {
            throw new IllegalArgumentException("Unregistered packet " + packet.getClass());
        }
    }

    public static Packet createFromId(byte id) {
        return suppliers.get(id).get();
    }
}
