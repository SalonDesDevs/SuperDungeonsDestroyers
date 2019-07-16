package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.components.Camera;
import org.salondesdevs.superdungeonsdestroyers.components.Position;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CameraSyncSystem extends IteratingSystem {

    @Inject
    Mapper<Position> positionMapper;

    @Inject
    CameraService cameraService;

    public CameraSyncSystem() {
        super(Aspect.all(Position.class, Camera.class));
    }

    @Override
    public void process(int entity) {
        Position position = positionMapper.get(entity);
        cameraService.camera.position.x = position.x;
        cameraService.camera.position.y = position.y;
        cameraService.camera.update();
    }
}
