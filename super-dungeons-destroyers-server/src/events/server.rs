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
    pub level: u8,
    pub environment: LevelEnvironment,
    pub entities: Vec<Entity>
}

#[derive(Debug)]
pub enum Event {
    Connect(Connect),
    Join(Join),
    Leave(Leave),
    EntityMove(EntityMove),
    ZoneInfo(ZoneInfo),
}
