use crate::events;
use crate::binding;

use flatbuffers::{ FlatBufferBuilder, WIPOffset as W };
use failure::Fallible;

pub trait FlatRead<'b, Item> where Self: Sized {
    fn read(item: Item) -> Fallible<Self>;
}

// Client implementations

impl<'b> FlatRead<'b, binding::client::Move<'b>> for events::client::Move {
    fn read(item: binding::client::Move<'b>) -> Fallible<Self> {
        unimplemented!()
    }
}

impl<'b> FlatRead<'b, binding::client::Event<'b>> for events::client::Event {
    fn read(item: binding::client::Event<'b>) -> Fallible<Self> {
        unimplemented!()
    }
}

// Common implementations

impl FlatRead<'_, binding::common::Direction> for events::common::Direction {
    fn read(item: binding::common::Direction) -> Fallible<Self> {
        unimplemented!()
    }
}

impl<'b> FlatRead<'b, binding::common::Player<'b>> for events::common::Player {
    fn read(item: binding::common::Player<'b>) -> Fallible<Self> {
        unimplemented!()
    }
}

impl FlatRead<'_, binding::common::Location> for events::common::Location {
    fn read(item: binding::common::Location) -> Fallible<Self> {
        unimplemented!()
    }
}

impl FlatRead<'_, binding::common::EntityKind> for events::common::EntityKind {
    fn read(item: binding::common::EntityKind) -> Fallible<Self> {
        unimplemented!()
    }
}

impl<'b> FlatRead<'b, binding::common::Entity<'b>> for events::common::Entity {
    fn read(item: binding::common::Entity<'b>) -> Fallible<Self> {
        unimplemented!()
    }
}

impl FlatRead<'_, binding::common::LevelEnvironment> for events::common::LevelEnvironment {
    fn read(item: binding::common::LevelEnvironment) -> Fallible<Self> {
        unimplemented!()
    }
}
