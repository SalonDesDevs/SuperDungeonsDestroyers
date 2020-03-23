# Super Dungeons Destroyers

A small and strategic 2D game where players have to cooperate to progress in dark dungeons.
The server is written in standard Java, whilst the client uses libgdx.

## Running

Use gradle to build project.

To run client :
```
gradlew desktop:run
```

To run server :
```
gradlew server:run
```

## Short introduction for contributors

### ECS 101
This project is designed with the *Entity Component System* pattern:
- **Entities** represent *any object* you can encounter in the world (player, mob, dropped item, trap, door, etc.). Every entity can be retrevied by its *id*, a positive integer.
- **Components** indicate some trait of an entity, and hold relevant data (position, size, name, color, etc.). 
- **Systems** contain the game logic (process the components).

A collection of traits (ie. a set of components *types*) is called an **Aspect** of an entity. For instance, an "entity that can move" would hold `Position` and `Velocity`, an entity renderer by an animated sprite would hold `Position` and `AnimatedSprite`.

Components are accessed by systems via **Mappers**, small pieces of code automatically injected into your systems.


#### An example
For instance, a basic continous movement system would consider of:

```java
class Position implements Component {
  float x, y;
}

class Velocity implements Component {
  float x, y;
}

class VelocitySystem extends ProcessingSystem {
  MotionSystem() {
    super(Aspect.all(Position.class, Velocity.class));
  }

  @Inject
  Mapper<Position> positionMapper;

  @Injet
  Mapper<Velocity> velocityMapper;

  @Inject
  World world;
  
  @Override
  public void process(int entity) {
    Position position = positionMapper.get(entity);
    Velocity velocity = velocityMapper.get(entity);

    position.x += velocity.x * world.getDelta();
    position.y += velocity.y * world.getDelta();
  }
}
```
*(for more information about what a `ProcessingSystem` is see javadoc)*

Later on, we could add a `GravitySystem` in the following way:
```java
class HasGravity implements Component {}

class GravitySystem extends ProcessingSystem {
  MotionSystem() {
    super(Aspect.all(Velocity.class, HasGravity.class));
  }

  @Injet
  Mapper<Velocity> velocityMapper;

  @Inject
  World world;
  
  @Override
  public void process(int entity) {
    Velocity velocity = velocityMapper.get(entity);
    velocity.y += 9.81f * world.getDelta();
  }
}
```
Note that the `HasGravity` component doesn't contain any data: we only use its presence or absence to indicate whether the entity should be impacted by gravity or not.

We touch on here the sweetness of ECS pattern, allowing us to split our game logic into (rather small) *semantic* classes: instead of having a very big `PhysicsSystem` we can combine smaller systems to achieve complex features.

