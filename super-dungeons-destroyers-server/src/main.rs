#![feature(async_await, vec_remove_item)]

pub mod binding;
pub mod network;
pub mod game;
pub mod utils;
pub mod events;
pub mod error;

use crate::network::Connection;
use crate::game::Context;
use crate::game::game_loop::GameLoop;

use tokio::prelude::*;
use tokio::net::TcpListener;
use tokio::timer::Interval;

use failure::{ Fallible, Error };

use std::time::Duration;
use log::{ info, error };

fn listener() -> Fallible<TcpListener> {
    let address = "127.0.0.1:9000".parse()?;

    info!("Server listening on {}", address);

    Ok(TcpListener::bind(&address)?)
}

fn main() -> Fallible<()> {
    pretty_env_logger::init();

    let context = Context::new();

    let mut game_loop = GameLoop::new(context.clone());

    let interval = Interval::new_interval(Duration::from_millis(100))
        .map_err(Error::from)
        .for_each(move |instant| Ok(game_loop.tick(instant)?));

    let server = listener()?
        .incoming()
        .map_err(Error::from)
        .for_each(move |socket| {
            info!("New socket");

            let connection = Connection::new(socket);

            tokio::spawn(connection.process(context.clone())?);

            Ok(())
        });

    let game = server
        .join(interval)
        .map(|_| ())
        .map_err(|error| error!("{}", error));

    tokio::run(game);

    Ok(())
}
