package org.salondesdevs.superdungeonsdestroyers.library.utils;

import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;

public class ProtocolVersion {

    public static final int MAJOR = 1;
    public static final int MINOR = 0;

    public static String string() {
        return toString(MAJOR, MINOR);
    }

    public static String toString(int major, int minor) {
        return major + "." + minor;
    }

    public static String toString(VersionCheck versionCheck) {
        return toString(versionCheck.major, versionCheck.minor);
    }
}
