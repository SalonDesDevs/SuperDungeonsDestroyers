#[derive(Debug)]
pub enum Direction {
    Right,
    Left,
    Down,
    Up,
}

<<<<<<< HEAD
#[derive(Debug)]
=======
#[derive(Debug, Clone)]
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
pub struct Player {
    pub name: String,
    pub location: Location,
}

<<<<<<< HEAD
#[derive(Debug)]
pub struct Location {
    pub level: u8,
    pub x: u8,
    pub y: u8
}

#[derive(Debug)]
=======
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
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
pub enum EntityKind {
    Player(Player)
}

<<<<<<< HEAD
#[derive(Debug)]
pub struct Entity {
    pub entity_id: u64,
    pub kind: EntityKind,
}

#[derive(Debug)]
=======
pub type EntityId = u64;

#[derive(Debug, Clone)]
pub struct Entity {
    pub entity_id: EntityId,
    pub kind: EntityKind,
}

#[derive(Debug, Clone, PartialEq, Eq, Hash)]
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
pub enum LevelEnvironment {
    Bottom,
    Cave,
    Top,
    CollisionsTester
}
<<<<<<< HEAD
=======

impl Entity {
    pub fn location(&self) -> &Location {
        match &self.kind {
            EntityKind::Player(player) => &player.location
        }
    }
}
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
