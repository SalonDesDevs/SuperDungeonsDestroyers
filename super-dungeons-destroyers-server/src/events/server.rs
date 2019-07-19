use super::common::{ Player, Location, Entity, LevelEnvironment, EntityId };

#[derive(Debug)]
pub struct Welcome {
    pub me: Entity,
}

#[derive(Debug)]
pub struct Join {
    pub player: Entity,
}

#[derive(Debug)]
pub struct Leave {
    pub player: Entity,
}

#[derive(Debug)]
pub struct EntityMove {
    pub entity_id: EntityId,
    pub location: Location,
}

#[derive(Debug)]
pub struct ZoneInfo {
    pub level: u8,
    pub environment: LevelEnvironment,
    pub entities: Vec<Entity>
}

#[derive(Debug)]
pub enum Event {
    Welcome(Welcome),
    Join(Join),
    Leave(Leave),
    EntityMove(EntityMove),
    ZoneInfo(ZoneInfo),
}
