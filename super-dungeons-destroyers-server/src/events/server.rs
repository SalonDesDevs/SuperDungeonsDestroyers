use super::common::{ Player, Location, Entity, LevelEnvironment };

pub struct Connect {
    pub my_entity_id: u64,
}

pub struct Join {
    pub player: Player,
}

pub struct Leave {
    pub player: Player,
}

pub struct EntityMove {
    pub entity_id: u64,
    pub location: Location,
}

pub struct ZoneInfo {
    pub level: u8,
    pub environment: LevelEnvironment,
    pub entities: Vec<Entity>
}

pub enum Event {
    Connect(Connect),
    Join(Join),
    Leave(Leave),
    EntityMove(EntityMove),
    ZoneInfo(ZoneInfo),
}
