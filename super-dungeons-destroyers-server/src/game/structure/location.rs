use crate::utils::WriteToBuilder;
use crate::binding::common;

use super::room::RoomId;

use flatbuffers::FlatBufferBuilder;

use failure::Fallible;

#[derive(Clone, Copy)]
pub struct Location {
    pub room: RoomId,
    pub x: u8,
    pub y: u8,
}

// TODO: Remove
impl Default for Location {
    fn default() -> Self {
        Location {
            room: 0,
            x: 0,
            y: 0
        }
    }
}

impl<'b> WriteToBuilder<'b, common::Location> for Location {
    fn write(&self, _: &mut FlatBufferBuilder<'b>) -> Fallible<common::Location> {
        let Location { room, x, y } = self;

        Ok(common::Location::new(*room, *x, *y))
    }
}
