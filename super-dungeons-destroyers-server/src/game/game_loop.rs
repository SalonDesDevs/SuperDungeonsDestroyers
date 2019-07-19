use super::Context;

use crate::binding::{ server, common };

use failure::Fallible;

use std::sync::Arc;
use std::time::Instant;

use flatbuffers::{ FlatBufferBuilder, WIPOffset };
use log::{ warn, debug };

pub struct GameLoop {
    context: Context
}

impl GameLoop {
    pub fn new(context: Context) -> Self {
        GameLoop {
            context
        }
    }

    pub fn tick(&mut self, instant: Instant) -> Fallible<()> {
        //self.send_environment()?;

        let elapsed = instant.elapsed();

        debug!("Ticked in {:6}µs", elapsed.as_micros());

        if elapsed.as_millis() >= 75 {
            warn!("[!] Last tick took too long!");
        }

        Ok(())
    }

    // fn send_environment(&mut self) -> Fallible<()> {
    //     let mut players = self.context.players.write().unwrap();
    //     let levels = self.context.levels.read().unwrap();

    //     let players_clone = players.clone();

    //     for (_, player) in players.iter_mut() {
    //         let mut builder = FlatBufferBuilder::new();

    //         let level = levels
    //             .get(player.location.level as usize)
    //             .unwrap()
    //             .write(&mut builder)?;

    //         let entities = players_clone
    //             .values()
    //             .filter(|target| target.location.level == player.location.level)
    //             .map(|target| target.write(&mut builder))
    //             .collect::<Fallible<Vec<WIPOffset<common::Entity>>>>()?;

    //         let entities = builder.create_vector(&entities);

    //         let environment = server::Environment::create(
    //             &mut builder,
    //             &server::EnvironmentArgs {
    //                 level: Some(level),
    //                 entities: Some(entities),
    //                 me: player.entity_id
    //             }
    //         );

    //         let message = server::Message::create(
    //             &mut builder,
    //             &server::MessageArgs {
    //                 content: Some(environment.as_union_value()),
    //                 content_type: server::Content::Environment
    //             }
    //         );

    //         let messages = builder.create_vector(&[message]);

    //         let messages = server::Messages::create(
    //             &mut builder,
    //             &server::MessagesArgs {
    //                 messages: Some(messages)
    //             }
    //         );

    //         builder.finish(messages, None);

    //         let messages = ServerMessages {
    //             bytes: builder.finished_data().into()
    //         };

    //         player.tx.try_send(messages)?;
    //     }

    //     Ok(())
    // }
}
