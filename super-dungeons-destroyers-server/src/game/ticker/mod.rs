use crate::events::server::Event;
use crate::network::ClientIdentifier;
use crate::error::NoneError;

use super::Context;

use std::time::Instant;
use std::collections::HashMap;

use failure::Fallible;

use log::{ warn, debug };

#[derive(Default)]
struct MessageCache {
    inner: HashMap<ClientIdentifier, Vec<Event>>
}

impl MessageCache {
    fn register(&mut self, identifier: ClientIdentifier) {
        if !self.inner.contains_key(&identifier) {
            self.inner.insert(identifier, Vec::new());
        }
    }

    fn push(&mut self, identifier: &ClientIdentifier, event: Event) {
        if let Some(events) = self.inner.get_mut(identifier) {
            events.push(event)
        }
    }

    fn into_iter(self) -> impl Iterator<Item = (ClientIdentifier, Vec<Event>)> {
        self.inner.into_iter()
    }
}

pub struct Ticker {
    context: Context
}

impl Ticker {
    pub fn new(context: Context) -> Self {
        Ticker {
            context
        }
    }

    pub fn tick(&self, instant: Instant) -> Fallible<()> {
        self.send_events()?;

        let elapsed = instant.elapsed();

        debug!("Ticked in {:6}µs", elapsed.as_micros());

        if elapsed.as_millis() >= 75 {
            warn!("[!] Last tick took too long!");
        }

        Ok(())
    }

    fn send_events(&self) -> Fallible<()> {
        let events = self.context.consume_events();
        let clients = self.context.clients();
        let entities = self.context.entities();

        let mut message_cache = MessageCache::default();

        for event in events {
            match event {
                Event::Welcome(welcome) => {
                    let (identifier, _) = clients
                        .iter()
                        .find(|&(identifier, _)| identifier == &welcome.me.entity_id)
                        .ok_or(NoneError)?;

                    message_cache.register(identifier.clone());
                    message_cache.push(&identifier, Event::Welcome(welcome));
                },

                Event::Join(join) => {
                    for (identifier, _) in clients.iter() {
                        message_cache.register(identifier.clone());
                        message_cache.push(&identifier, Event::Join(join.clone()))
                    }
                },

                Event::Leave(leave) => {
                    for (identifier, _) in clients.iter() {
                        message_cache.register(identifier.clone());
                        message_cache.push(&identifier, Event::Leave(leave.clone()))
                    }
                },

                Event::EntityMove(r#move) => {
                    use crate::events::server::EntityMove;
                    use crate::events::common::EntityKind;

                    let EntityMove { location, .. } = r#move;

                    for (identifier, _) in clients.iter() {
                        let player = entities.get(&identifier.entity_id).ok_or(NoneError)?;

                        if let EntityKind::Player(player) = &player.kind {
                            if player.location.level == location.level {
                                message_cache.register(identifier.clone());
                                message_cache.push(&identifier, Event::EntityMove(r#move.clone()))
                            }
                        }
                    }
                },

                Event::ZoneInfo(_) => {
                    unimplemented!()
                }
            }
        }

        for (client, events) in message_cache.into_iter() {
            let (_, client) = clients.iter()
                .find(|&(identifier, _)| identifier == &client.address)
                .ok_or(NoneError)?;

            client.sender.clone().try_send(events)?;
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
