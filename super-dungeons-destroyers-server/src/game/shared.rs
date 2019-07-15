use crate::network::Tx;

use std::net::SocketAddr;
use std::collections::HashMap;

#[derive(Default)]
pub struct Shared {
    pub players: HashMap<SocketAddr, Player>,
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
