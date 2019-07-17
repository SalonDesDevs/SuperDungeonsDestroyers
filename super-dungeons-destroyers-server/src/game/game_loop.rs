use super::structure::{ Shared, Room, Location, RoomKind };

use crate::binding::{ server, common };
use crate::network::ServerMessages;
use crate::utils::WriteToBuilder;

use failure::Fallible;

use std::sync::Arc;
use std::time::Instant;
use std::convert::TryInto;

use flatbuffers::{ FlatBufferBuilder, WIPOffset };

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
            self.initialization()?;
        }

        self.update_location();
        self.send_environment()?;

        let elapsed = instant.elapsed();

        eprintln!("Ticked in {:6}µs", elapsed.as_micros());

        if elapsed.as_millis() >= 75 {
            eprintln!("[!] Last tick took too long!");
        }

        Ok(())
    }

    fn initialization(&mut self) -> Fallible<()> {
        let mut rooms = self.shared.rooms.lock().unwrap();

        let lines = vec![
            RoomKind::Top,
            RoomKind::Bottom,
            RoomKind::Cave
        ];

        for (id, kind) in lines.into_iter().enumerate() {
            let id = id.try_into()?;

            rooms.insert(id, Room { id, kind });
        }

        Ok(())
    }

    fn update_location(&mut self) {
        let mut players = self.shared.players.lock().unwrap();
        let rooms = self.shared.rooms.lock().unwrap();

        for (_, player) in players.iter_mut() {
            let room = if rooms.is_empty() { None } else { Some(()) };
            let player_location = player.location
                .map_or(Location::default(), |location| Location {
                    room: 0,
                    x: location.x + 1 % 10,
                    y: location.y + 1 % 10
                });

            player.location = room.and(Some(player_location));
        }
    }

    fn send_environment(&mut self) -> Fallible<()> {
        let mut players = self.shared.players.lock().unwrap();
        let rooms = self.shared.rooms.lock().unwrap();

        let players_clone = players.clone();

        for (_, player) in players.iter_mut() {
            let mut builder = FlatBufferBuilder::new();

            let location = player.location.unwrap();

            let room = rooms
                .get(&location.room)
                .unwrap()
                .write(&mut builder)?;

            let entities = players_clone
                .values()
                .filter(|target| target.location.map(|target_location| target_location.room == location.room).unwrap_or(false))
                .map(|target| target.write(&mut builder))
                .collect::<Fallible<Vec<WIPOffset<common::Entity>>>>()?;

            let entities = builder.create_vector(&entities);

            let environment = server::Environment::create(
                &mut builder,
                &server::EnvironmentArgs {
                    room: Some(room),
                    entities: Some(entities)
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
