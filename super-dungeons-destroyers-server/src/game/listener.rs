use crate::network::{ ClientMessages, ServerMessages, Peer };
use crate::binding::{ client, server, common };
use crate::game::structure::{ Location };

use std::io;

use failure::Fallible;

use flatbuffers::{ WIPOffset, FlatBufferBuilder };

use log::{ debug, info };

use std::cmp::{ max, min };

pub struct Listener;

type ServerMessage<'b> = WIPOffset<server::Message<'b>>;

impl Listener {
    pub fn handle_messages(peer: &Peer, messages: ClientMessages) -> Fallible<()> {
        let ClientMessages { messages, .. } = messages;
        let messages = messages.messages();

        let mut builder = FlatBufferBuilder::new();
        let mut server_messages = Vec::new();

        for message in 0..messages.len() {
            let message = messages.get(message);

            let result = Listener::handle_message(peer, message, &mut builder)?;
            server_messages.extend(result);
        }

        let server_messages = builder.create_vector(&server_messages);
        let server_messages = server::Messages::create(
            &mut builder,
            &server::MessagesArgs {
                messages: Some(server_messages)
            }
        );

        builder.finish(server_messages, None);

        let server_messages = ServerMessages {
            bytes: builder.finished_data().into()
        };

        peer.tx.clone().try_send(server_messages)?;

        Ok(())
    }

    pub fn handle_message<'b>(peer: &Peer, message: client::Message, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<Vec<ServerMessage<'b>>> {
        use client::Content;

        match message.content_type() {
            Content::Ping => {
                let message = message.content_as_ping().unwrap();

                debug!("{}", message.value());

                let pong = server::Pong::create(
                    &mut builder,
                    &server::PongArgs {
                        value: message.value()
                    }
                );

                let response = server::Message::create(
                    &mut builder,
                    &server::MessageArgs {
                        content: Some(pong.as_union_value()),
                        content_type: server::Content::Pong
                    }
                );

                Ok(vec![response])
            },

            Content::Move => {
                let message = message.content_as_move().unwrap();
                let direction = message.direction();

                debug!("{} moved {:?}", peer.address, direction);

                let mut players = peer.shared.players.write().unwrap();
                let player = players.get_mut(&peer.address).unwrap();

                if let Some(ref mut location) = player.location {
                    let levels = peer.shared.levels.read().unwrap();

                    let x_move = match direction {
                        common::Direction::Right => 1,
                        common::Direction::Left => -1,
                        _ => 0,
                    };

                    let y_move = match direction {
                        common::Direction::Down => 1,
                        common::Direction::Up => -1,
                        _ => 0,
                    };

                    let current_level = levels.get(location.level as usize).unwrap();

                    let future_location = Location {
                        level: location.level,
                        x: min(max(location.x as i32 + x_move, 0), (current_level.inner_map.width - 1) as i32) as u8,
                        y: min(max(location.y as i32 + y_move, 0), (current_level.inner_map.height - 1) as i32) as u8
                    };

                    info!("Move? {:?} {:?} {:?}", location, future_location, !current_level.solid_locations.contains(&future_location));

                    if !current_level.solid_locations.contains(&future_location) {
                        *location = future_location;
                    }
                }

                Ok(Vec::new())
            },

            Content::NONE => {
                Err(io::Error::from(io::ErrorKind::InvalidData))?
            }
        }
    }
}
