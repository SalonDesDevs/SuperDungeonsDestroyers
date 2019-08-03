use crate::game::{ Context, Listener };
use crate::events::common::EntityId;

<<<<<<< HEAD
use crate::game::structure::{ Context, Player };
use crate::game::listener::Listener;
=======
use super::Sender;
use super::codec::MessageCodec;
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51

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

<<<<<<< HEAD
pub struct Peer {
    pub context: Arc<Context>,

=======
#[derive(Clone)]
pub struct Client {
    pub id: EntityId,
    pub context: Context,
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
    pub address: SocketAddr,
    pub sender: Sender
}

<<<<<<< HEAD
impl Peer {
    fn new(address: SocketAddr, context: Arc<Context>, tx: Tx) -> Fallible<Self> {
        let player = Player::new(address.clone(), tx.clone(), context.clone())?;

        context.players.write().unwrap().insert(address, player);

        Ok(Peer {
            context,
            address,
            tx,
        })
=======
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
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
    }
}


impl Connection {
    pub fn new(socket: TcpStream) -> Self {
        Connection {
            socket
        }
    }

<<<<<<< HEAD
    pub fn process(self, context: Arc<Context>) -> Fallible<impl Future<Item = (), Error = ()>> {
        let address = self.socket.peer_addr().unwrap();
        let (tx, rx) = mpsc::unbounded_channel();
        let peer = Peer::new(address, context.clone(), tx)?;
=======
    pub fn process(self, context: Context) -> Fallible<impl Future<Item = (), Error = ()>> {
        let address = self.socket.peer_addr().unwrap();
        let (tx, rx) = mpsc::unbounded_channel();
        let client = Client::new(address, context.clone(), tx)?;
>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
        let (sink, stream) = Framed::new(self.socket, MessageCodec::default()).split();
        let listener = Listener::new(client);

        let to_client = rx
            .map_err(Error::from)
            .inspect(|_| debug!("Sending message(s) to the client."))
            .forward(sink);

        let from_client = stream
<<<<<<< HEAD
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

=======
            .inspect(|_| debug!("Received event(s) from the client"))
            .for_each(move |messages| listener.process_events(messages))
            .inspect(|_| info!("Client disconnection"))
            .and_then(move |_| Ok(context.disconnection(address)));

>>>>>>> 183748109ca379a6acd1bccec75a63cb63bdbd51
        let future = to_client.join(from_client)
            .map(|_| ())
            .map_err(|error| error!("{}", error));

        Ok(future)
    }
}
