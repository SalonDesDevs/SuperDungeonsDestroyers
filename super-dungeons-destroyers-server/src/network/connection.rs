use crate::game::{ Context, Listener };
use crate::events::common::EntityId;

use super::Sender;
use super::codec::MessageCodec;

use tokio::prelude::*;
use tokio::sync::mpsc;
use tokio::net::TcpStream;
use tokio::codec::Framed;

use std::net::SocketAddr;

use failure::{ Error, Fallible };

use log::{ error, debug, info } ;

pub struct Connection {
    socket: TcpStream
}

#[derive(Clone)]
pub struct Client {
    pub id: EntityId,
    pub context: Context,
    pub address: SocketAddr,
    pub sender: Sender
}

impl Client {
    fn new(address: SocketAddr, context: Context, sender: Sender) -> Fallible<Self> {
        let client = Client {
            id: context.create_entity_id(),
            context,
            address,
            sender
        };

        client.context.register_client(client.clone())?;

        Ok(client)
    }
}


impl Connection {
    pub fn new(socket: TcpStream) -> Self {
        Connection {
            socket
        }
    }

    pub fn process(self, context: Context) -> Fallible<impl Future<Item = (), Error = ()>> {
        let address = self.socket.peer_addr().unwrap();
        let (tx, rx) = mpsc::unbounded_channel();
        let client = Client::new(address, context.clone(), tx)?;
        let (sink, stream) = Framed::new(self.socket, MessageCodec::default()).split();
        let listener = Listener::new(client);

        let to_client = rx
            .map_err(Error::from)
            .inspect(|_| debug!("Sending message(s) to the client."))
            .forward(sink);

        let from_client = stream
            .inspect(|_| debug!("Received event(s) from the client"))
            .for_each(move |messages| listener.process_events(messages))
            .inspect(|_| info!("Client disconnection"))
            .and_then(move |_| Ok(context.disconnection(address)));

        let future = to_client.join(from_client)
            .map(|_| ())
            .map_err(|error| error!("{}", error));

        Ok(future)
    }
}
