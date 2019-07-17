pub mod player;
pub mod location;
pub mod room;

use crate::network::ServerMessages;

use std::sync::Mutex;
use std::collections::HashMap;
use std::net::SocketAddr;

pub use self::room::{ Room, RoomKind, RoomId };
pub use self::player::Player;
pub use self::location::Location;

use failure::Fallible;

#[derive(Default)]
pub struct Shared {
    pub players: Mutex<HashMap<SocketAddr, Player>>,
    pub rooms: Mutex<HashMap<RoomId, Room>>,
}

impl Shared {
    pub fn broadcast(&self, message: ServerMessages) -> Fallible<()> {
        for (_, player) in self.players.lock().unwrap().iter_mut() {
            player.tx.try_send(message.clone())?;
        }

        Ok(())
    }
}
