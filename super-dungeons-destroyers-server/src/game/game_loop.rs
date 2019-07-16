use super::shared::{ Shared, Room };
use super::room;

use crate::binding::{ server, common };
use crate::network::ServerMessages;

use failure::Fallible;

use std::sync::Arc;

use flatbuffers::FlatBufferBuilder;

pub struct GameLoop;

impl GameLoop {
    pub fn tick(shared: Arc<Shared>) -> Fallible<()> {
        eprintln!("Game tick!");

        let mut players = shared.players.lock().unwrap();
        let mut rooms = shared.rooms.lock().unwrap();

        if rooms.is_empty() {
            let lines = vec![
                (0, room::RoomKind::Top),
                (1, room::RoomKind::Bottom),
                (2, room::RoomKind::Cave)
            ];

            for (id, kind) in lines {
                rooms.insert(id, Room { id, kind });
            }
        }

        for (_, player) in players.iter_mut() {
            let room = if rooms.is_empty() { None } else { Some(()) };
            let player_room = player.room
                .map_or(0, |x| (x + 1) % rooms.len());

            player.room = room.and(Some(player_room));

            if player.room.is_none() {
                continue;
            }

            let mut builder = FlatBufferBuilder::new();

            let room = rooms.get(&player.room.unwrap()).unwrap();
            let room = common::Room::create(
                &mut builder,
                &common::RoomArgs {
                    kind: room.kind.clone().into()
                }
            );

            let environment = server::Environment::create(
                &mut builder,
                &server::EnvironmentArgs {
                    room: Some(room)
                }
            );

            let message = server::Message::create(
                &mut builder,
                &server::MessageArgs {
                    content: Some(environment.as_union_value()),
                    content_type: server::Content::Pong
                }
            );

            let messages = builder.create_vector(&[message]);

            let messages = server::Messages::create(
                &mut builder,
                &server::MessagesArgs {
                    messages: Some(messages)
                }
            );

            builder.finish(messages, None);

            let messages = ServerMessages {
                bytes: builder.finished_data().into()
            };

            player.tx.try_send(messages)?;
        }


        //shared.broadcast(messages)?;

        Ok(())
    }
}
