mod codec;
mod connection;

use crate::events::server::Event;

use tokio::sync::mpsc;

pub use connection::{ Connection, Client };

pub type Receiver = mpsc::UnboundedReceiver<Vec<Event>>;
pub type Sender = mpsc::UnboundedSender<Vec<Event>>;
