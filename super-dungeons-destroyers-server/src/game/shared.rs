use super::room::RoomKind;

use crate::network::{ Tx, ServerMessages };

use std::net::SocketAddr;
use std::collections::HashMap;

use failure::Fallible;

#[derive(Default)]
pub struct Shared {
    pub players: HashMap<SocketAddr, Player>,
    pub rooms: HashMap<usize, Room>,
}

pub struct Room {
    pub id: usize,
    pub kind: RoomKind,
}

pub struct Player {
    pub name: String,

    pub tx: Tx,
    pub address: SocketAddr,
}

impl Player {
    pub fn new(address: SocketAddr, tx: Tx) -> Self {
        Player {
            name: String::from("VF > *"),
            tx,
            address
        }
    }
}

impl Shared {
    pub fn broadcast(&mut self, message: ServerMessages) -> Fallible<()> {
        for (_, player) in &mut self.players {
            player.tx.try_send(message.clone())?;
        }

        Ok(())
    }
}
