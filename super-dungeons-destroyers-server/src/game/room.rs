use std::collections::HashMap;
use std::path::Path;

use tiled::{ parse_file, Map };

use failure::Fallible;

const TESTMAP: &'static str = "../../../commons/tiledmaps/testmap.tmx";

#[derive(Eq, PartialEq, Hash)]
pub enum RoomKind {
    TestMap
}

impl AsRef<Path> for RoomKind {
    fn as_ref(&self) -> &Path {
        match self {
            RoomKind::TestMap => Path::new(TESTMAP)
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
