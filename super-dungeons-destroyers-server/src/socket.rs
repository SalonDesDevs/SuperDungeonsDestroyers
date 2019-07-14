use std::sync::{ Mutex, Arc };
use std::collections::HashMap;
use std::net::Shutdown;
use std::io;

use tokio::prelude::*;
use tokio::net::TcpStream;
use tokio::codec::Framed;

use crate::codec::{ Codec, RequestData };

type Identifier = usize;

#[derive(Clone, Debug)]
pub struct Socket(Arc<Mutex<SocketInner>>);

#[derive(Clone, Debug)]
pub struct Shared(Arc<Mutex<SharedInner>>);

#[derive(Debug)]
struct SocketInner {
    id: Identifier,
    stream: TcpStream,
    shared: Shared
}
#[derive(Debug)]
struct SharedInner {
    sockets: HashMap<Identifier, Socket>,
    id_counter: Identifier,
}

impl Default for Shared {
    fn default() -> Self {
        let shared = SharedInner {
            sockets: HashMap::new(),
            id_counter: 0
        };

        Shared(Arc::new(Mutex::new(shared)))
    }
}

impl Shared {
    fn generate_id(&self) -> Identifier {
        let mut shared = self.0.lock().unwrap();
        shared.id_counter += 1;
        shared.id_counter - 1
    }
}

impl Socket {
    pub fn new(stream: TcpStream, shared: Shared) -> Self {
        let socket = SocketInner {
            id: shared.generate_id(),
            stream,
            shared
        };

        let socket = Socket(Arc::new(Mutex::new(socket)));
        socket.register();
        socket
    }

    fn register(&self) {
        let socket = self.0.lock().unwrap();
        let mut shared = socket.shared.0.lock().unwrap();

        shared.sockets.insert(socket.id, self.clone());
    }

    pub fn disconnect(&self) {
        let socket = self.0.lock().unwrap();
        let mut shared = socket.shared.0.lock().unwrap();

        shared.sockets.remove(&socket.id);
    }

    pub fn handle(self) -> impl Future<Item = (), Error = ()> {
        Framed::new(self.clone(), Codec::default())
            .for_each(|request| {
                let RequestData { request, .. } = request;

                println!("Got {} task(s)!", request.tasks().len());

                for task in 0..request.tasks().len() {
                    let task = request.tasks().get(task);
                    println!("Action type is {:?}", task.action_type());
                }

                Ok(())
            })
            .and_then(move |_| {
                println!("Socket quit");

                self.disconnect();

                Ok(())
            })
            .map_err(|_| ())
    }
}

impl Read for Socket {
    fn read(&mut self, buffer: &mut [u8]) -> io::Result<usize> {
        self.0.lock().unwrap().stream.read(buffer)
    }
}

impl Write for Socket {
    fn write(&mut self, buffer: &[u8]) -> io::Result<usize> {
        self.0.lock().unwrap().stream.write(buffer)
    }

    fn flush(&mut self) -> io::Result<()> {
        Ok(())
    }
}

impl AsyncWrite for Socket {
    fn shutdown(&mut self) -> Poll<(), io::Error> {
        self.0.lock().unwrap().stream.shutdown(Shutdown::Write)?;
        Ok(().into())
    }
}

impl AsyncRead for Socket {}
