package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class DisconnectReason extends Packet {
    public String reason;

    public DisconnectReason(String reason) {
        this.reason = reason;
    }
}
