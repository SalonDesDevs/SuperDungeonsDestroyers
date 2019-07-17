pub mod player;
pub mod location;
pub mod level;
pub mod staticSolid;

use crate::network::ServerMessages;

use std::sync::{ Mutex, RwLock };
use std::collections::HashMap;
use std::net::SocketAddr;

pub use self::level::{ Level, LevelKind };
pub use self::player::Player;
pub use self::location::Location;

use failure::Fallible;

#[derive(Default)]
pub struct Shared {
    pub players: RwLock<HashMap<SocketAddr, Player>>,
    pub levels: RwLock<Vec<Level>>,

    last_entity_id: Mutex<u64>
}

impl Shared {
    pub fn broadcast(&self, message: ServerMessages) -> Fallible<()> {
        for (_, player) in self.players.read().unwrap().iter() {
            player.tx.clone().try_send(message.clone())?;
        }

        Ok(())
    }

    pub fn next_entity_id(&self) -> u64 {
        let mut id = self.last_entity_id.lock().unwrap();
        *id += 1;
        *id - 1
    }
}
