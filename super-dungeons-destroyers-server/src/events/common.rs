#[derive(Debug)]
pub enum Direction {
    Right,
    Left,
    Down,
    Up,
}

#[derive(Debug)]
pub struct Player {
    pub name: String,
    pub location: Location,
}

#[derive(Debug)]
pub struct Location {
    pub level: u8,
    pub x: u8,
    pub y: u8
}

#[derive(Debug)]
pub enum EntityKind {
    Player(Player)
}

#[derive(Debug)]
pub struct Entity {
    pub entity_id: u64,
    pub kind: EntityKind,
}

#[derive(Debug)]
pub enum LevelEnvironment {
    Bottom,
    Cave,
    Top,
    CollisionsTester
}
