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

    pub level: u8,
    pub environment: LevelEnvironment,
    pub entities: Vec<Entity>
}

#[derive(Debug, Clone)]
pub enum Event {
    Welcome(Welcome),
    Join(Join),
    Leave(Leave),
    EntityMove(EntityMove),
    ZoneInfo(ZoneInfo),
}
