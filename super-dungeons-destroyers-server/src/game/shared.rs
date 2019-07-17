use super::room::RoomKind;

use crate::network::{ Tx, ServerMessages };

use std::net::SocketAddr;
use std::collections::HashMap;
use std::sync::Mutex;

use failure::Fallible;

#[derive(Default)]
pub struct Shared {
    pub players: Mutex<HashMap<SocketAddr, Player>>,
    pub rooms: Mutex<HashMap<usize, Room>>,
}

pub struct Room {
    pub id: usize,
    pub kind: RoomKind,
}

#[derive(Clone, Copy)]
pub struct Location {
    pub room: usize,
    pub x: usize,
    pub y: usize
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

#[derive(Clone)]
pub struct Player {
    pub name: String,
    pub location: Option<Location>,

    pub tx: Tx,
    pub address: SocketAddr,
}

impl Player {
    pub fn new(address: SocketAddr, tx: Tx) -> Self {
        Player {
            name: String::from("VF > *"),
            location: None,
            tx,
            address
        }
    }
}

impl Shared {
    pub fn broadcast(&self, message: ServerMessages) -> Fallible<()> {
        for (_, player) in self.players.lock().unwrap().iter_mut() {
            player.tx.try_send(message.clone())?;
        }

        Ok(())
    }
}
