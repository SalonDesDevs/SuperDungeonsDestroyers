package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class Welcome extends Packet {
    public int me;

    public Welcome(int me) {
        this.me = me;
    }
}
