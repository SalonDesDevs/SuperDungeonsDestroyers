#![feature(async_await, vec_remove_item)]

use tokio::prelude::*;
use tokio::net::TcpListener;
use failure::Fallible;

fn listener() -> Fallible<TcpListener> {
    let address = "127.0.0.1:9000".parse()?;

    Ok(TcpListener::bind(&address)?)
}

fn main() -> Fallible<()> {
    let server = listener()?
        .incoming()
        .map_err(|_error| ())
        .for_each(move |_socket| {
            println!("New socket");

            Ok(())
        });

    tokio::run(server);

    Ok(())
}
