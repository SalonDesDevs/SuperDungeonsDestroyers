<<<<<<< HEAD
use super::common::{ Player, Location, Entity, LevelEnvironment };

#[derive(Debug)]
pub struct Connect {
    pub my_entity_id: u64,
}

#[derive(Debug)]
pub struct Join {
    pub player: Player,
}

#[derive(Debug)]
pub struct Leave {
    pub player: Player,
}

#[derive(Debug)]
pub struct EntityMove {
    pub entity_id: u64,
    pub location: Location,
}

#[derive(Debug)]
pub struct ZoneInfo {
=======
use super::common::{ Location, Entity, LevelEnvironment, EntityId };

#[derive(Debug, Clone)]
pub struct Welcome {
    pub me: Entity,
}

#[derive(Debug, Clone)]
pub struct Join {
    pub player: Entity,
}

#[derive(Debug, Clone)]
pub struct Leave {
    pub player: Entity,
}

#[derive(Debug, Clone)]
pub struct EntityMove {
    pub entity_id: EntityId,
    pub location: Location,
}

#[derive(Debug, Clone)]
pub struct ZoneInfo {
    pub receiver: EntityId,

>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
    pub level: u8,
    pub environment: LevelEnvironment,
    pub entities: Vec<Entity>
}

<<<<<<< HEAD
#[derive(Debug)]
pub enum Event {
    Connect(Connect),
=======
#[derive(Debug, Clone)]
pub enum Event {
    Welcome(Welcome),
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
    Join(Join),
    Leave(Leave),
    EntityMove(EntityMove),
    ZoneInfo(ZoneInfo),
}
