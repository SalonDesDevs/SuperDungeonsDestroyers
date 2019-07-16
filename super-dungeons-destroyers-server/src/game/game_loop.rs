use super::shared::{ Shared, Room };
use super::room;

use crate::binding::{ server, common };
use crate::network::ServerMessages;

use failure::Fallible;

use std::sync::Arc;
use std::time::Instant;

use flatbuffers::FlatBufferBuilder;

pub struct GameLoop {
    shared: Arc<Shared>,
    initialized: bool
}

impl GameLoop {
    pub fn new(shared: Arc<Shared>) -> Self {
        GameLoop {
            shared,
            initialized: false,
        }
    }

    pub fn tick(&mut self, instant: Instant) -> Fallible<()> {
        if !self.initialized {
            self.initialization();
        }

        self.update_location();
        self.send_environment()?;

        eprintln!("Ticked in {:6}µs", instant.elapsed().as_micros());

        Ok(())
    }

    fn initialization(&mut self) {
        let mut rooms = self.shared.rooms.lock().unwrap();

        let lines = vec![
            room::RoomKind::Top,
            room::RoomKind::Bottom,
            room::RoomKind::Cave
        ];

        for (id, kind) in lines.into_iter().enumerate() {
            rooms.insert(id, Room { id, kind });
        }
    }

    fn update_location(&mut self) {
        let mut players = self.shared.players.lock().unwrap();
        let rooms = self.shared.rooms.lock().unwrap();

        for (_, player) in players.iter_mut() {
            let room = if rooms.is_empty() { None } else { Some(()) };
            let player_room = player.room
                .map_or(0, |x| (x + 1) % rooms.len());

            player.room = room.and(Some(player_room));
        }
    }

    fn send_environment(&mut self) -> Fallible<()> {
        let mut players = self.shared.players.lock().unwrap();
        let rooms = self.shared.rooms.lock().unwrap();

        for (_, player) in players.iter_mut() {
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
                    content_type: server::Content::Environment
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

        Ok(())
    }
}
