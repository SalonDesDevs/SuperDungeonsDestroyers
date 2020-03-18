package org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient;

import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

import io.netty.buffer.ByteBuf;

public class PlayerName extends Packet {

    private String name;

    public PlayerName() {
    }

    public PlayerName(String name) {
        this.name = name;
    }

    @Override
    public void read(ByteBuf in) {
        this.name = readString(in);
    }

    @Override
    public void write(ByteBuf out) {
        writeString(name, out);
    }

    public String getName() {
        return name;
    }
}
