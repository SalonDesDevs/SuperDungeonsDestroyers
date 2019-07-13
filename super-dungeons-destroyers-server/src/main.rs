#![feature(async_await, vec_remove_item)]

use tokio::prelude::*;
use tokio::net::TcpListener;
use tokio::io::write_all;
use failure::Fallible;

mod socket;
use socket::{ Shared, Socket };

fn listener() -> Fallible<TcpListener> {
    let address = "127.0.0.1:9000".parse()?;

    Ok(TcpListener::bind(&address)?)
}

fn main() -> Fallible<()> {
    let shared = Shared::default();

    let server = listener()?
        .incoming()
        .map_err(|_error| ())
        .for_each(move |socket| {
            println!("New socket");

            let socket = Socket::new(socket, shared.clone());
            let task = write_all(socket, b"acheter un micro a cedric")
                .map(|_| ())
                .map_err(|_| ());

            tokio::spawn(task)
        });

    tokio::run(server);

    Ok(())
}
