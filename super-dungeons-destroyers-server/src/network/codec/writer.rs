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

impl FlatWrite<'_, binding::common::Direction> for events::common::Direction {
    fn write(&self, builder: &mut FlatBufferBuilder) -> Fallible<binding::common::Direction> {
        unimplemented!()
    }
}

impl<'b> FlatWrite<'b, W<binding::common::Player<'b>>> for events::common::Player {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<binding::common::Player<'b>>> {
        unimplemented!()
    }
}

impl FlatWrite<'_, binding::common::Location> for events::common::Location {
    fn write(&self, builder: &mut FlatBufferBuilder) -> Fallible<binding::common::Location> {
        unimplemented!()
    }
}

impl FlatWrite<'_, binding::common::EntityKind> for events::common::EntityKind {
    fn write(&self, builder: &mut FlatBufferBuilder) -> Fallible<binding::common::EntityKind> {
        unimplemented!()
    }
}

impl<'b> FlatWrite<'b, W<binding::common::Entity<'b>>> for events::common::Entity {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<W<binding::common::Entity<'b>>> {
        unimplemented!()
    }
}

impl FlatWrite<'_, binding::common::LevelEnvironment> for events::common::LevelEnvironment {
    fn write(&self, builder: &mut FlatBufferBuilder) -> Fallible<binding::common::LevelEnvironment> {
        unimplemented!()
    }
}
