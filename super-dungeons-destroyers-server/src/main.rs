#![feature(async_await, vec_remove_item)]

mod network;
mod game;
mod binding;

use crate::network::Connection;
use crate::game::shared::Shared;

use tokio::prelude::*;
use tokio::net::TcpListener;

use failure::{ Fallible, Error };

use std::sync::{ Arc, Mutex };

fn listener() -> Fallible<TcpListener> {
    let address = "127.0.0.1:9000".parse()?;

    Ok(TcpListener::bind(&address)?)
}

fn main() -> Fallible<()> {
    let shared = Arc::new(Mutex::new(Shared::default()));

    let server = listener()?
        .incoming()
        .for_each(move |socket| {
            println!("New socket");

            let connection = Connection::new(socket);

            tokio::spawn(connection.process(shared.clone()));

            Ok(())
        })
        .map_err(Error::from)
        .map_err(|error| {
            for cause in error.as_fail().iter_causes() {
                eprintln!("{}", cause);
            }
        });

    println!("Server is listening!");

    tokio::run(server);

    Ok(())
}
