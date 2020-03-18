package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Levels;

import io.netty.buffer.ByteBuf;

public class SwitchLevel extends Packet {

    public Levels level;

    public SwitchLevel() {
    }

    public SwitchLevel(Levels level) {
        this.level = level;
    }

    @Override
    public void read(ByteBuf in) {
        this.level = readEnum(Levels.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        writeEnum(this.level, out);
    }
}
