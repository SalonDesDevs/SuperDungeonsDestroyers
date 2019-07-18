use super::Tx;
use super::codec::MessageCodec;

use crate::game::structure::{ Context, Player };
use crate::game::listener::Listener;

use tokio::prelude::*;
use tokio::sync::mpsc;
use tokio::net::TcpStream;
use tokio::codec::Framed;

use std::sync::Arc;
use std::net::SocketAddr;

use failure::{ Error, Fallible };

use log::{ error, warn, debug } ;

pub struct Connection {
    socket: TcpStream
}

pub struct Peer {
    pub context: Arc<Context>,

    pub address: SocketAddr,
    pub tx: Tx,
}

impl Peer {
    fn new(address: SocketAddr, context: Arc<Context>, tx: Tx) -> Fallible<Self> {
        let player = Player::new(address.clone(), tx.clone(), context.clone())?;

        context.players.write().unwrap().insert(address, player);

        Ok(Peer {
            context,
            address,
            tx,
        })
    }
}


impl Connection {
    pub fn new(socket: TcpStream) -> Self {
        Connection {
            socket
        }
    }

    pub fn process(self, context: Arc<Context>) -> Fallible<impl Future<Item = (), Error = ()>> {
        let address = self.socket.peer_addr().unwrap();
        let (tx, rx) = mpsc::unbounded_channel();
        let peer = Peer::new(address, context.clone(), tx)?;
        let (sink, stream) = Framed::new(self.socket, MessageCodec::default()).split();

        let to_client = rx
            .map_err(Error::from)
            .inspect(|_| debug!("Sending message(s) to the client."))
            .forward(sink)
            .map_err(Error::from);

        let from_client = stream
            .map_err(Error::from)
            .for_each(move |messages| {
                debug!("Received message(s) from the client.");

                Listener::handle_messages(&peer, messages)
            })
            .and_then(move |_| {
                warn!("Got disconnected");

                context.players.write().unwrap().remove(&address);

                Ok(())
            });

        let future = to_client.join(from_client)
            .map(|_| ())
            .map_err(|error| error!("{}", error));

        Ok(future)
    }
}
