package org.salondesdevs.superdungeonsdestroyers.library.packets;

import com.esotericsoftware.kryo.Kryo;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.EntityMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.DisconnectReason;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.Welcome;

public class Packet {

    public static void registerToKryo(Kryo kryo) {

        // From client
        kryo.register(VersionCheck.class);
        kryo.register(EntityMove.class);

        // From server
        kryo.register(DisconnectReason.class);
        kryo.register(Welcome.class);
    }
}
