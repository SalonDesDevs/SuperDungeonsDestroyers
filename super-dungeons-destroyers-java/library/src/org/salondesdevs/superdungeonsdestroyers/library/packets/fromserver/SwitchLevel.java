package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.packets.ByteBufHelper;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Levels;

public class SwitchLevel extends Packet {

    public Levels level;

    public SwitchLevel() {
    }

    public SwitchLevel(Levels level) {
        this.level = level;
    }

    @Override
    public void read(ByteBuf in) {
        this.level = ByteBufHelper.readEnum(Levels.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufHelper.writeEnum(this.level, out);
    }
}
