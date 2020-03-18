package org.salondesdevs.superdungeonsdestroyers.library.packets;

import java.util.function.Supplier;

import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.FromClientChat;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerName;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.*;
import org.salondesdevs.superdungeonsdestroyers.library.utils.NettySerializable;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;

public abstract class Packet implements NettySerializable {

    private static final Byte2ObjectMap<Supplier<? extends Packet>> suppliers = new Byte2ObjectOpenHashMap<>();
    private static final Object2ByteMap<Class<? extends Packet>> classes = new Object2ByteOpenHashMap<>();

    static {
        // Both sides
        int id = 0;
        register(id++, KeepAlive::new, KeepAlive.class);

        id = 10;

        // From client
        register(id++, VersionCheck::new, VersionCheck.class);
        register(id++, PlayerMove::new, PlayerMove.class);
        register(id++, PlayerName::new, PlayerName.class);

        id = 50;

        // From server
        register(id++, VersionCheckSuccess::new, VersionCheckSuccess.class);
        register(id++, DisconnectReason::new, DisconnectReason.class);
        register(id++, Welcome::new, Welcome.class);
        register(id++, EntitySpawn::new, EntitySpawn.class);
        register(id++, EntityDespawn::new, EntityDespawn.class);
        register(id++, EntityTeleport::new, EntityTeleport.class);
        register(id++, EntityMove::new, EntityMove.class);
        register(id++, SwitchLevel::new, SwitchLevel.class);
        register(id++, EntityComponentSet::new, EntityComponentSet.class);
        register(id++, FromClientChat::new, FromClientChat.class);
        register(id++, FromServerChat::new, FromServerChat.class);
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
