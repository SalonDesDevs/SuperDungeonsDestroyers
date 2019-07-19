mod codec;
mod connection;

use crate::events::common::EntityId;
use crate::events::server::Event;

use tokio::sync::mpsc;

use std::net::SocketAddr;

pub use connection::{ Connection, Client };

pub type Receiver = mpsc::UnboundedReceiver<Vec<Event>>;
pub type Sender = mpsc::UnboundedSender<Vec<Event>>;

#[derive(PartialEq, Eq, Hash, Clone)]
pub struct ClientIdentifier {
    pub address: SocketAddr,
    pub entity_id: EntityId
}

impl PartialEq<SocketAddr> for ClientIdentifier {
    fn eq(&self, address: &SocketAddr) -> bool {
        self.address == *address
    }
}

impl PartialEq<EntityId> for ClientIdentifier {
    fn eq(&self, entity_id: &EntityId) -> bool {
        self.entity_id == *entity_id
    }
}
