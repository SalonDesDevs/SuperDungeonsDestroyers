package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import org.salondesdevs.superdungeonsdestroyers.library.events.Event;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class PacketReceivedEvent extends Event {
    private final Packet packet;

    public PacketReceivedEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    @Override
    public String toString() {
        return "PacketReceivedEvent[" + this.packet.getClass().getSimpleName() + "]";
    }
}
