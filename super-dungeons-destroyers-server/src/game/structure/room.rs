use crate::binding::common;
use crate::utils::WriteToBuilder;

use failure::Fallible;

use flatbuffers::{ FlatBufferBuilder, WIPOffset };

pub type RoomId = u8;

#[derive(Eq, PartialEq, Hash, Clone)]
pub enum RoomKind {
    Bottom,
    Cave,
    Top
}

pub struct Room {
    pub id: RoomId,
    pub kind: RoomKind,
}

impl Into<common::RoomKind> for RoomKind {
    fn into(self) -> common::RoomKind {
        match self {
            RoomKind::Bottom => common::RoomKind::Bottom,
            RoomKind::Cave => common::RoomKind::Cave,
            RoomKind::Top => common::RoomKind::Top
        }
    }
}

impl<'b> WriteToBuilder<'b, WIPOffset<common::Room<'b>>> for Room {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<WIPOffset<common::Room<'b>>> {
        let room = common::Room::create(
            &mut builder,
            &common::RoomArgs {
                kind: self.kind.clone().into()
            }
        );

        Ok(room)
    }
}
