package org.salondesdevs.superdungeonsdestroyers.library.systems;

import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import net.wytrem.ecs.World;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.components.Position;
import org.salondesdevs.superdungeonsdestroyers.library.components.Speed;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;

/**
 * Transforms {@link EntityKind} into components sets. This class is overriden on both client and server sides to allow
 * placing side-specific components (Sprite & Animation for client, Tracked and PlayerConnection for server, etc.).
 *
 * @see EntityCreator#spawn(EntityKind)
 */
public abstract class EntityCreator extends Service {

    @Inject
    World world;

    protected Map<EntityKind, IntConsumer> componentsAdders;

    @Override
    public void initialize() {
        componentsAdders = new HashMap<>();
        componentsAdders.put(EntityKind.PLAYER, this::addPlayerComponents);
    }

    @Inject
    Mapper<EntityKind> entityKindMapper;

    protected void addEntityKind(int entity, EntityKind kind) {
        entityKindMapper.set(entity, kind);
    }

    @Inject
    Mapper<Position> positionMapper;

    protected void addPosition(int entity) {
        positionMapper.set(entity, new Position());
    }

    @Inject
    Mapper<Speed> speedMapper;

    private void addSpeed(int entity) {
        speedMapper.set(entity, new Speed());
    }

    protected void addPlayerComponents(int entity) {
        addBasicComponents(entity);
    }

    /**
     * Creates a new entity of the given {@link EntityKind}. Adds all relevant {@link net.wytrem.ecs.Component}s.
     *
     * @return the new entity
     */
    public int spawn(EntityKind entityKind) {
        int entity = world.createEntity();

        addComponents(entity, entityKind);
        return entity;
    }

    public void addComponents(int entity, EntityKind entityKind) {
        addEntityKind(entity, entityKind);

        if (this.componentsAdders.containsKey(entityKind)) {
            this.componentsAdders.get(entityKind).accept(entity);
        }
    }

    protected void addBasicComponents(int entity) {
        addPosition(entity);
        addSpeed(entity);
    }
}
