#![feature(async_await, vec_remove_item)]

use tokio::prelude::*;
use tokio::net::TcpListener;
use failure::Fallible;

mod socket;
use socket::{ Shared, Socket };

mod codec;

fn listener() -> Fallible<TcpListener> {
    let address = "127.0.0.1:9000".parse()?;

    Ok(TcpListener::bind(&address)?)
}

fn main() -> Fallible<()> {
    let shared = Shared::default();

    let server = listener()?
        .incoming()
        .for_each(move |socket| {
            println!("New socket");

            let socket = Socket::new(socket, shared.clone());
            tokio::spawn(socket.handle());

            Ok(())
        })
        .map_err(|_error| ());

    println!("Server is listening!");

    use flatbuffers::FlatBufferBuilder;
    use sdd::request::{ Request, RequestArgs, Action, Ping, PingArgs, Task, TaskArgs };

    let mut builder = FlatBufferBuilder::new();

    let ping = Ping::create(
        &mut builder,
        &PingArgs {
            value: 42
        }
    );

    let task = Task::create(
        &mut builder,
        &TaskArgs {
            action_type: Action::Ping,
            action: Some(ping.as_union_value())
        }
    );

    let tasks = builder.create_vector(&[task]);

    let request = Request::create(
        &mut builder,
        &RequestArgs {
            tasks: Some(tasks)
        }
    );

    builder.finish(request, None);

    let buf = builder.finished_data();

    let hexas = (buf.len() as u32)
        .to_le_bytes()
        .iter()
        .chain(buf)
        .map(|x| format!("\\x{:x}", x))
        .collect::<String>();

    println!("{}", hexas);

    tokio::run(server);

    Ok(())
}
