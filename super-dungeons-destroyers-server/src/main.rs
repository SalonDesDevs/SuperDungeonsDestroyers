#![feature(async_await, vec_remove_item)]

pub mod network;
pub mod game;
pub mod binding;

use crate::network::Connection;
use crate::game::shared::Shared;
use crate::game::game_loop::GameLoop;

use tokio::prelude::*;
use tokio::net::TcpListener;
use tokio::timer::Interval;

use failure::{ Fallible, Error };

use std::sync::{ Arc, Mutex };
use std::time::Duration;

fn listener() -> Fallible<TcpListener> {
    let address = "127.0.0.1:9000".parse()?;

    Ok(TcpListener::bind(&address)?)
}

fn main() -> Fallible<()> {
    let shared = Arc::new(Mutex::new(Shared::default()));
    let shared_interval = shared.clone();

    let interval = Interval::new_interval(Duration::from_millis(1000))
        .map_err(Error::from)
        .for_each(move |_| GameLoop::tick(shared_interval.clone()));

    let server = listener()?
        .incoming()
        .map_err(Error::from)
        .for_each(move |socket| {
            eprintln!("New socket");

            let connection = Connection::new(socket);

            tokio::spawn(connection.process(shared.clone()));

            Ok(())
        });

    eprintln!("Server is listening!");

    let game = server
        .join(interval)
        .map(|_| ())
        .map_err(|error| {
            for cause in error.as_fail().iter_causes() {
                eprintln!("{}", cause);
            }
        });

    tokio::run(game);

    Ok(())
}
