package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Position;
import org.salondesdevs.superdungeonsdestroyers.components.Sprited;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityCreator extends Service {

    @Inject
    Mapper<Sprited> spritedMapper;

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    Assets assets;

    public void addSprited(int entity, Sprited.Sprites sprites) {
        this.spritedMapper.set(entity, new Sprited(sprites.toTextureRegion(assets.tileset)));
    }
}
