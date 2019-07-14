#![feature(async_await, vec_remove_item)]

use tokio::prelude::*;
use tokio::net::TcpListener;
use tokio::codec::Framed;
use failure::Fallible;

mod socket;
use socket::{ Shared, Socket };

mod codec;
use codec::{ Codec, RequestData };

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
            let framed = Framed::new(socket.clone(), Codec::default());
            let connection = framed
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

                    socket.disconnect();

                    Ok(())
                })
                .map_err(|_| ());

            tokio::spawn(connection);

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
