pub enum Direction {
    Right,
    Left,
    Down,
    Up,
}

pub struct Player {
    pub name: String,
    pub location: Location,
}

pub struct Location {
    pub level: u8,
    pub x: u8,
    pub y: u8
}

pub enum EntityKind {
    Player(Player)
}

pub struct Entity {
    pub entity_id: u64,
    pub kind: EntityKind,
}

pub enum LevelEnvironment {
    Bottom,
    Cave,
    Top,
    CollisionsTester
}
