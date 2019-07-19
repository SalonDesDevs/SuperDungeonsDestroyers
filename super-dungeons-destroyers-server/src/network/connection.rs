use super::Tx;
use super::codec::MessageCodec;

use crate::game::Context;
use crate::game::listener::Listener;
use crate::events::server::{ Event, Welcome };

use tokio::prelude::*;
use tokio::sync::mpsc;
use tokio::net::TcpStream;
use tokio::codec::Framed;

use std::sync::Arc;
use std::net::SocketAddr;

use failure::{ Error, Fallible };

use log::{ error, warn, debug, info } ;

pub struct Connection {
    socket: TcpStream
}

#[derive(Clone)]
pub struct Client {
    pub context: Context,
    pub address: SocketAddr,
    pub sender: Tx,
}

impl Client {
    fn new(address: SocketAddr, context: Context, sender: Tx) -> Fallible<Self> {
        let client = Client {
            context,
            address,
            sender,
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
        let peer = Client::new(address, context.clone(), tx)?;
        let (sink, stream) = Framed::new(self.socket, MessageCodec::default()).split();

        let to_client = rx
            .map_err(Error::from)
            .inspect(|_| debug!("Sending message(s) to the client."))
            .forward(sink);

        let from_client = stream
            .for_each(move |messages| {
                debug!("Received message(s) from the client.");

                Listener::handle_messages(&peer, messages)
            })
            .and_then(move |_| {
                info!("Got disconnected");

                // context.players.write().unwrap().remove(&address);
                // TODO: Remove client from context

                Ok(())
            });

        let future = to_client.join(from_client)
            .map(|_| ())
            .map_err(|error| error!("{}", error));

        Ok(future)
    }
}
