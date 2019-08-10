package org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient;

import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class VersionCheck extends Packet {
    public int major, minor;

    public VersionCheck() {

    }

    public VersionCheck(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }
}
