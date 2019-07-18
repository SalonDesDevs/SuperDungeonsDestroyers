pub mod player;
pub mod location;
pub mod level;

use crate::network::ServerMessages;

use std::sync::{ Mutex, RwLock };
use std::collections::HashMap;
use std::net::SocketAddr;

pub use self::level::{ Level, LevelKind };
pub use self::player::Player;
pub use self::location::Location;

use failure::Fallible;

pub struct Context {
    pub players: RwLock<HashMap<SocketAddr, Player>>,
    pub levels: RwLock<Vec<Level>>,

    last_entity_id: Mutex<u64>
}

impl Context {
    pub fn new() -> Fallible<Self> {
        let players = HashMap::new();
        let levels = vec![Level::new(0, LevelKind::Top)?];
        let context = Context {
            players: RwLock::new(players),
            levels: RwLock::new(levels),
            last_entity_id: Mutex::new(0)
        };

        Ok(context)
    }

    pub fn broadcast(&self, message: ServerMessages) -> Fallible<()> {
        for (_, player) in self.players.read().unwrap().iter() {
            player.send(message.clone())?;
        }

        Ok(())
    }

    pub fn next_entity_id(&self) -> u64 {
        let mut id = self.last_entity_id.lock().unwrap();
        *id += 1;
        *id - 1
    }
}
