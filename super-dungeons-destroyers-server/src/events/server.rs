use super::common::{ Player, Location, Entity, LevelEnvironment };

pub struct Connect {
    my_entity_id: u64,
}

pub struct Join {
    player: Player,
}

pub struct Leave {
    player: Player,
}

pub struct EntityMove {
    entity_id: u64,
    location: Location,
}

pub struct ZoneInfo {
    level: u8,
    environment: LevelEnvironment,
    entities: Vec<Entity>
}

pub enum Event {
    Connect(Connect),
    Join(Join),
    Leave(Leave),
    EntityMove(EntityMove),
    ZoneInfo(ZoneInfo),
}
