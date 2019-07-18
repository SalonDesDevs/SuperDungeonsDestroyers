pub enum Direction {
    Right,
    Left,
    Down,
    Up,
}

pub struct Player {
    name: String,
    location: Location,
}

pub struct Location {
    level: u8,
    x: u8,
    y: u8
}

pub enum EntityKind {
    Player(Player)
}

pub struct Entity {
    entity_id: usize,
    kind: EntityKind,
}

pub enum LevelEnvironment {
    Bottom,
    Cave,
    Top,
    CollisionsTester
}
