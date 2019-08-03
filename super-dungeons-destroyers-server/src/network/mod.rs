mod codec;
mod connection;

<<<<<<< HEAD
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
=======
use crate::events::server::Event;

use tokio::sync::mpsc;

pub use connection::{ Connection, Client };
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51

pub type Receiver = mpsc::UnboundedReceiver<Vec<Event>>;
pub type Sender = mpsc::UnboundedSender<Vec<Event>>;
