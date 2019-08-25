package org.salondesdevs.superdungeonsdestroyers.library.utils;

import io.netty.buffer.ByteBuf;
import net.wytrem.ecs.Component;

import java.nio.charset.Charset;

public interface NettySerializable {
    void read(ByteBuf in);

    void write(ByteBuf out);

    Charset utf8 = Charset.forName("utf-8");

    default void writeString(String str, ByteBuf out) {
        out.writeInt(str.length());
        out.writeCharSequence(str, utf8);
    }

    default String readString(ByteBuf in) {
        return in.readCharSequence(in.readInt(), utf8).toString();
    }

    default  <E extends Enum<E>> void writeEnum(E value, ByteBuf out) {
        out.writeByte(value.ordinal());
    }

    default  <E extends Enum<E>> E readEnum(Class<E> clazz, ByteBuf in) {
        return clazz.getEnumConstants()[in.readByte()];
    }

    default void writeWatchableComponent(Component watchableComponent, ByteBuf out) {
        out.writeByte(WatchedComponents.getId(watchableComponent));

        if (Enum.class.isAssignableFrom(watchableComponent.getClass())) {
            writeEnum((Enum) watchableComponent, out);
        }
        else {
            ((NettySerializable) watchableComponent).write(out);
        }
    }

    default Component readWatchableComponent(ByteBuf in) {
        byte id = in.readByte();

        if (Enum.class.isAssignableFrom(WatchedComponents.getClassById(id))) {
            return (Component) readEnum((Class<? extends Enum>) WatchedComponents.getClassById(id), in);
        }
        else {
            Component read = WatchedComponents.createFromId(id);
            ((NettySerializable) read).read(in);
            return read;
        }
    }
}