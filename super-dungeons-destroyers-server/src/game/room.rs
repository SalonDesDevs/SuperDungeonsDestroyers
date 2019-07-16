use crate::binding::common;

use std::collections::HashMap;
use std::path::Path;

use tiled::{ parse_file, Map };

use failure::Fallible;

const BOTTOM_MAP: &'static str = "../../../commons/rooms/bottom.tmx";
const CAVE_MAP: &'static str = "../../../commons/rooms/cave.tmx";
const TOP_MAP: &'static str = "../../../commons/rooms/top.tmx";

#[derive(Eq, PartialEq, Hash, Clone)]
pub enum RoomKind {
    Bottom,
    Cave,
    Top
}

impl AsRef<Path> for RoomKind {
    fn as_ref(&self) -> &Path {
        match self {
            RoomKind::Bottom => Path::new(BOTTOM_MAP),
            RoomKind::Cave => Path::new(CAVE_MAP),
            RoomKind::Top => Path::new(TOP_MAP)
        }
    }
}

pub struct Loader {
    rooms: HashMap<RoomKind, Map>
}

impl Default for Loader {
    fn default() -> Self {
        Loader {
            rooms: HashMap::new()
        }
    }
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

impl Loader {
    pub fn load(&mut self, room: RoomKind) -> Fallible<()> {
        if self.rooms.contains_key(&room) {
            return Ok(());
        }

        let map = parse_file(room.as_ref())?;
        self.rooms.insert(room, map);

        Ok(())
    }
}
