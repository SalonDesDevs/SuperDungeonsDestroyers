use crate::utils::WriteToBuilder;
use crate::binding::common;

use flatbuffers::FlatBufferBuilder;

use failure::Fallible;

#[derive(Clone, Copy)]
pub struct Location {
    pub level: u8,
    pub x: u8,
    pub y: u8,
}

// TODO: Remove
impl Default for Location {
    fn default() -> Self {
        Location {
            level: 0,
            x: 0,
            y: 0
        }
    }
}

impl<'b> WriteToBuilder<'b, common::Location> for Location {
    fn write(&self, _: &mut FlatBufferBuilder<'b>) -> Fallible<common::Location> {
        let Location { level, x, y } = self;

        Ok(common::Location::new(*level, *x, *y))
    }
}
