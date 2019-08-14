package org.salondesdevs.superdungeonsdestroyers.library.packets;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class ByteBufHelper {

    private static final Charset utf8 = Charset.forName("utf-8");

    public static void writeString(String str, ByteBuf out) {
        out.writeInt(str.length());
        out.writeCharSequence(str, utf8);
    }

    public static String readString(ByteBuf in) {
        return in.readCharSequence(in.readInt(), utf8).toString();
    }
}
