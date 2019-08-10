package org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient;

import org.salondesdevs.superdungeonsdestroyers.library.components.Direction;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class EntityMove extends Packet {
    public Direction direction;

    public EntityMove(Direction direction) {
        this.direction = direction;
    }
}
