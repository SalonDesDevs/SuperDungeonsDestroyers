use super::structure::{ Shared, Level, LevelKind };

use crate::binding::{ server, common };
use crate::network::ServerMessages;
use crate::utils::WriteToBuilder;

use failure::Fallible;

use std::sync::Arc;
use std::time::Instant;

use flatbuffers::{ FlatBufferBuilder, WIPOffset };
use log::{ info, error, warn };
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
        info!("Ticked in {:6}µs", elapsed.as_micros());

        if elapsed.as_millis() >= 75 {
            warn!("[!] Last tick took too long!");
        }

        Ok(())
    }

    fn initialization(&mut self) -> Fallible<()> {
        let mut levels = self.shared.levels.write().unwrap();

        levels.push(Level::new(0, LevelKind::Top, self.shared.clone())?);

        Ok(())
    }

    fn update_location(&mut self) {
        let mut players = self.shared.players.write().unwrap();
        let levels = self.shared.levels.read().unwrap();

        let level = match levels.get(0) {
            Some(level) => level,
            None => return
        };

        for player in players.values_mut() {
            if player.location.is_some() {
                continue;
            }

            player.location = level.spawnpoints.get(0).cloned();
        }
    }

    fn send_environment(&mut self) -> Fallible<()> {
        let mut players = self.shared.players.write().unwrap();
        let levels = self.shared.levels.read().unwrap();

        let players_clone = players.clone();

        for (_, player) in players.iter_mut() {
            let mut builder = FlatBufferBuilder::new();

            let location = player.location.unwrap();

            let level = levels
                .get(location.level as usize)
                .unwrap()
                .write(&mut builder)?;

            let entities = players_clone
                .values()
                .filter(|target| target.location.map(|target_location| target_location.level == location.level).unwrap_or(false))
                .map(|target| target.write(&mut builder))
                .collect::<Fallible<Vec<WIPOffset<common::Entity>>>>()?;

            let entities = builder.create_vector(&entities);

            let environment = server::Environment::create(
                &mut builder,
                &server::EnvironmentArgs {
                    level: Some(level),
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
