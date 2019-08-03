#[derive(Debug)]
pub enum Direction {
    Right,
    Left,
    Down,
    Up,
}

#[derive(Debug, Clone)]
pub struct Player {
    pub name: String,
    pub location: Location,
}

#[derive(Debug, Clone, Copy)]
pub struct Coordinates {
    pub x: u8,
    pub y: u8,
}

#[derive(Debug, Clone, Copy)]
pub struct Location {
    pub level: u8,
    pub coordinates: Coordinates
}

#[derive(Debug, Clone)]
pub enum EntityKind {
    Player(Player)
}

pub type EntityId = u64;

#[derive(Debug, Clone)]
pub struct Entity {
    pub entity_id: EntityId,
    pub kind: EntityKind,
}

#[derive(Debug, Clone, PartialEq, Eq, Hash)]
pub enum LevelEnvironment {
    Bottom,
    Cave,
    Top,
    CollisionsTester
}

impl Entity {
    pub fn location(&self) -> &Location {
        match &self.kind {
            EntityKind::Player(player) => &player.location
        }
    }
}
