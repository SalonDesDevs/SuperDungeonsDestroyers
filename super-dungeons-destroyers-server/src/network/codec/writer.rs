use crate::events;
use crate::binding;

use flatbuffers::{ FlatBufferBuilder, WIPOffset as W };
use failure::Fallible;

pub trait FlatWrite<'b, Item> {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<Item>;
}

// Server implementations

impl<'b> FlatWrite<'b, W<binding::server::Connect<'b>>> for events::server::Connect {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<binding::server::Connect<'b>>> {
        unimplemented!()
    }
}

impl<'b> FlatWrite<'b, W<binding::server::Join<'b>>> for events::server::Join {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<binding::server::Join<'b>>> {
        unimplemented!()
    }
}

impl<'b> FlatWrite<'b, W<binding::server::Leave<'b>>> for events::server::Leave {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<binding::server::Leave<'b>>> {
        unimplemented!()
    }
}

impl<'b> FlatWrite<'b, W<binding::server::ZoneInfo<'b>>> for events::server::ZoneInfo {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<binding::server::ZoneInfo<'b>>> {
        unimplemented!()
    }
}

impl<'b> FlatWrite<'b, W<binding::server::EntityMove<'b>>> for events::server::EntityMove {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<binding::server::EntityMove<'b>>> {
        unimplemented!()
    }
}

impl<'b> FlatWrite<'b, W<binding::server::Event<'b>>> for events::server::Event {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<binding::server::Event<'b>>> {
        unimplemented!()
    }
}

// Common implementations

use binding::common::Direction as BDirection;
use events::common::Direction as EDirection;

impl FlatWrite<'_, BDirection> for EDirection {
    fn write(&self, _: &mut FlatBufferBuilder) -> Fallible<BDirection> {
        let direction = match self {
            EDirection::Up => BDirection::Up,
            EDirection::Down => BDirection::Down,
            EDirection::Right => BDirection::Right,
            EDirection::Left => BDirection::Left
        };

        Ok(direction)
    }
}

use binding::common::Player as BPlayer;
use events::common::Player as EPlayer;

impl<'b> FlatWrite<'b, W<BPlayer<'b>>> for EPlayer {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<BPlayer<'b>>> {
        let name = builder.create_string(&self.name);
        let location = self.location.write(&mut builder)?;

        let player = BPlayer::create(
            &mut builder,
            &binding::common::PlayerArgs {
                name: Some(name),
                location: Some(&location)
            }
        );

        Ok(player)
    }
}

use binding::common::Location as BLocation;
use events::common::Location as ELocation;

impl FlatWrite<'_, BLocation> for ELocation {
    fn write(&self, _: &mut FlatBufferBuilder) -> Fallible<BLocation> {
        let location = BLocation::new(self.level, self.x, self.y);

        Ok(location)
    }
}

use binding::common::{ Entity as BEntity, EntityKind as BEntityKind };
use events::common::{ Entity as EEntity, EntityKind as EEntityKind };

impl<'b> FlatWrite<'b, W<BEntity<'b>>> for EEntity {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<BEntity<'b>>> {
        let (kind_type, kind) = match &self.kind {
            EEntityKind::Player(player) => (BEntityKind::Player, player.write(&mut builder)?)
        };

        let entity = BEntity::create(
            &mut builder,
            &binding::common::EntityArgs {
                entity_id: self.entity_id,
                kind: Some(kind.as_union_value()),
                kind_type
            }
        );

        Ok(entity)
    }
}

use binding::common::LevelEnvironment as BLevelEnvironment;
use events::common::LevelEnvironment as ELevelEnvironment;

impl FlatWrite<'_, BLevelEnvironment> for ELevelEnvironment {
    fn write(&self, _: &mut FlatBufferBuilder) -> Fallible<BLevelEnvironment> {
        let environment = match self {
            ELevelEnvironment::Bottom => BLevelEnvironment::Bottom,
            ELevelEnvironment::Cave => BLevelEnvironment::Cave,
            ELevelEnvironment::Top => BLevelEnvironment::Top,
            ELevelEnvironment::CollisionsTester => BLevelEnvironment::CollisionsTester,
        };

        Ok(environment)
    }
}
