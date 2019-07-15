use super::Tx;
use super::codec::MessageCodec;

use crate::game::shared::{ Shared, Player };
use crate::game::listener::Listener;

use tokio::prelude::*;
use tokio::sync::mpsc;
use tokio::net::TcpStream;
use tokio::codec::Framed;

use std::sync::{ Arc, Mutex };
use std::net::SocketAddr;

use failure::Error;

pub struct Connection {
    socket: TcpStream
}

pub struct Peer {
    pub shared: Arc<Mutex<Shared>>,

    pub address: SocketAddr,
    pub tx: Tx,
}

impl Peer {
    fn new(address: SocketAddr, shared: Arc<Mutex<Shared>>, tx: Tx) -> Self {
        let player = Player::new(address.clone(), tx.clone());

        shared.lock().unwrap().players.insert(address, player);

        Peer {
            shared,
            address,
            tx,
        }
    }
}


impl Connection {
    pub fn new(socket: TcpStream) -> Self {
        Connection {
            socket
        }
    }

    pub fn process(self, shared: Arc<Mutex<Shared>>) -> impl Future<Item = (), Error = ()> {
        let address = self.socket.peer_addr().unwrap();
        let (tx, rx) = mpsc::unbounded_channel();
        let peer = Peer::new(address, shared.clone(), tx);
        let (sink, stream) = Framed::new(self.socket, MessageCodec::default()).split();

        let to_client = rx
            .map_err(Error::from)
            .inspect(|_| eprintln!("Sending message(s) to the client."))
            .forward(sink)
            .map_err(Error::from);

        let from_client = stream
            .map_err(Error::from)
            .for_each(move |messages| {
                eprintln!("Received message(s) from the client.");

                Listener::handle_messages(&peer, messages)
            });

        to_client.join(from_client)
            .map(|_| ())
            .map_err(|error| {
                for cause in error.as_fail().iter_causes() {
                    eprintln!("{}", cause);
                }
            })
    }
}
