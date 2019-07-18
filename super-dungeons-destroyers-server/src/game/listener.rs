use crate::network::{ Peer };
use crate::game::structure::{ Location };
use crate::events::client;

use std::io;

use failure::Fallible;

use flatbuffers::{ WIPOffset, FlatBufferBuilder };

use log::debug;

use std::cmp::{ max, min };

pub struct Listener;

// type ServerMessage<'b> = WIPOffset<server::Message<'b>>;

impl Listener {
    pub fn handle_messages(peer: &Peer, events: Vec<client::Event>) -> Fallible<()> {
        // let ClientMessages { messages, .. } = messages;
        // let messages = messages.messages();

        // let mut builder = FlatBufferBuilder::new();
        // let mut server_messages = Vec::new();

        // for message in 0..messages.len() {
        //     let message = messages.get(message);

        //     let result = Listener::handle_message(peer, message, &mut builder)?;
        //     server_messages.extend(result);
        // }

        // let server_messages = builder.create_vector(&server_messages);
        // let server_messages = server::Messages::create(
        //     &mut builder,
        //     &server::MessagesArgs {
        //         messages: Some(server_messages)
        //     }
        // );

        // builder.finish(server_messages, None);

        // let server_messages = ServerMessages {
        //     bytes: builder.finished_data().into()
        // };

        // peer.tx.clone().try_send(server_messages)?;

        Ok(())
    }

    // pub fn handle_message<'b>(peer: &Peer, message: client::Message, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<Vec<ServerMessage<'b>>> {
    //     use client::Content;

    //     match message.content_type() {
    //         Content::Ping => {
    //             let message = message.content_as_ping().unwrap();

    //             debug!("{}", message.value());

    //             let pong = server::Pong::create(
    //                 &mut builder,
    //                 &server::PongArgs {
    //                     value: message.value()
    //                 }
    //             );

    //             let response = server::Message::create(
    //                 &mut builder,
    //                 &server::MessageArgs {
    //                     content: Some(pong.as_union_value()),
    //                     content_type: server::Content::Pong
    //                 }
    //             );

    //             Ok(vec![response])
    //         },

    //         Content::Move => {
    //             let message = message.content_as_move().unwrap();
    //             let direction = message.direction();

    //             debug!("{} moved {:?}", peer.address, direction);

    //             let mut players = peer.context.players.write().unwrap();
    //             let levels = peer.context.levels.read().unwrap();

    //             let player = players.get_mut(&peer.address).unwrap();

    //             let x_move = match direction {
    //                 common::Direction::Right => 1,
    //                 common::Direction::Left => -1,
    //                 _ => 0,
    //             };

    //             let y_move = match direction {
    //                 common::Direction::Down => 1,
    //                 common::Direction::Up => -1,
    //                 _ => 0,
    //             };

    //             let current_level = levels.get(player.location.level as usize).unwrap();

    //             let future_location = Location {
    //                 level: player.location.level,
    //                 x: min(max(player.location.x as i32 + x_move, 0), (current_level.inner_map.width - 1) as i32) as u8,
    //                 y: min(max(player.location.y as i32 + y_move, 0), (current_level.inner_map.height - 1) as i32) as u8
    //             };

    //             let can_move = !current_level.solid_locations.contains(&future_location);

    //             debug!("Move? {:?} {:?} {:?}", player.location, future_location, can_move);

    //             if can_move {
    //                 player.location = future_location;
    //             }

    //             Ok(Vec::new())
    //         },

    //         Content::NONE => {
    //             Err(io::Error::from(io::ErrorKind::InvalidData))?
    //         }
    //     }
    // }
}
