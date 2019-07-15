mod codec;
mod connection;

use tokio::sync::mpsc;

use bytes::Bytes;

use crate::binding::client;

pub struct ClientMessages {
    _bytes: Bytes,
    pub messages: client::Messages<'static>
}

pub struct ServerMessages {
    pub bytes: Bytes,
}

pub type Rx = mpsc::UnboundedReceiver<ServerMessages>;
pub type Tx = mpsc::UnboundedSender<ServerMessages>;

pub use connection::{ Connection, Peer };
