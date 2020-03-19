package org.salondesdevs.superdungeonsdestroyers.server.events;

import org.salondesdevs.superdungeonsdestroyers.library.events.PlayerEvent;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class PacketReceivedEvent extends PlayerEvent {
    private final Packet packet;

    public PacketReceivedEvent(int player, Packet packet) {
        super(player);
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    @Override
    public String toString() {
        return "PacketReceivedEvent[" + this.packet.getClass().getSimpleName() + "] from " + this.getPlayer();
    }
}
