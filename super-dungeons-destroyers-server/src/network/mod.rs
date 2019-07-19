mod codec;
mod connection;

use tokio::sync::mpsc;

use bytes::Bytes;

use crate::events::server::Event;

// pub struct ClientMessages {
//     _bytes: Bytes,
//     pub messages: client::Messages<'static>
// }
//
// #[derive(Debug, Clone)]
// pub struct ServerMessages {
//     pub bytes: Bytes,
// }

pub type Rx = mpsc::UnboundedReceiver<Vec<Event>>;
pub type Tx = mpsc::UnboundedSender<Vec<Event>>;

pub use connection::{ Connection, Client };
