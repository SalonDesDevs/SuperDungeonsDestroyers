package org.salondesdevs.superdungeonsdestroyers.library.utils;

import io.netty.buffer.ByteBuf;
import net.wytrem.ecs.Component;
import org.checkerframework.checker.units.qual.A;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.AutoWatched;
import org.salondesdevs.superdungeonsdestroyers.library.components.watched.AutoWatchedComponents;

import java.nio.charset.Charset;

public interface NettySerializable {
    void read(ByteBuf in);

    void write(ByteBuf out);

    Charset utf8 = Charset.forName("utf-8");

    default void writeString(String str, ByteBuf out) {
        Utils.writeUTF8String(out, str);
    }

    default String readString(ByteBuf in) {
        return Utils.readUTF8String(in);
    }

    default  <E extends Enum<E>> void writeEnum(E value, ByteBuf out) {
        out.writeByte(value.ordinal());
    }

    default  <E extends Enum<E>> E readEnum(Class<E> clazz, ByteBuf in) {
        return clazz.getEnumConstants()[in.readByte()];
    }

    default void writeWatchableComponent(AutoWatched watchableComponent, ByteBuf out) {
        out.writeByte(AutoWatchedComponents.getId(watchableComponent));

        if (Enum.class.isAssignableFrom(watchableComponent.getClass())) {
            writeEnum((Enum) watchableComponent, out);
        }
        else {
            ((NettySerializable) watchableComponent).write(out);
        }
    }

    default AutoWatched readWatchableComponent(ByteBuf in) {
        byte id = in.readByte();

        if (Enum.class.isAssignableFrom(AutoWatchedComponents.getClassById(id))) {
            return (AutoWatched) readEnum((Class<? extends Enum>) AutoWatchedComponents.getClassById(id), in);
        }
        else {
            AutoWatched read = AutoWatchedComponents.createFromId(id);
            ((NettySerializable) read).read(in);
            return read;
        }
    }
}
