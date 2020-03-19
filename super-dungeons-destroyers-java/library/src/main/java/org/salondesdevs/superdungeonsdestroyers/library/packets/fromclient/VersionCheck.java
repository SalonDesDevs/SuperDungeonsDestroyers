package org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class VersionCheck extends Packet {
    public int major, minor;

    public VersionCheck() {

    }

    public VersionCheck(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    @Override
    public void read(ByteBuf in) {
        this.major = in.readInt();
        this.minor = in.readInt();
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.major);
        out.writeInt(this.minor);
    }
}
