use crate::events::common::EntityId;
use crate::events::server::Event;
use crate::error::NoneError;

use super::Context;

use std::time::Instant;
use std::collections::HashMap;

use failure::Fallible;

use log::{ warn, debug };

#[derive(Default)]
struct MessageCache {
    inner: HashMap<EntityId, Vec<Event>>
}

impl MessageCache {
    fn register(&mut self, id: EntityId) {
        if !self.inner.contains_key(&id) {
            self.inner.insert(id, Vec::new());
        }
    }

    fn push(&mut self, id: &EntityId, event: Event) {
        if let Some(events) = self.inner.get_mut(id) {
            events.push(event)
        }
    }

    fn into_iter(self) -> impl Iterator<Item = (EntityId, Vec<Event>)> {
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

        if events.is_empty() {
            return Ok(());
        }

        let clients = self.context.clients();
        let entities = self.context.entities();

        let mut message_cache = MessageCache::default();

        for event in events {
            match event {
                Event::Welcome(welcome) => {
                    let id = welcome.me.entity_id;

                    message_cache.register(id);
                    message_cache.push(&id, Event::Welcome(welcome));
                },

                Event::Join(join) => {
                    for (id, _) in clients.iter() {
                        message_cache.register(id.clone());
                        message_cache.push(&id, Event::Join(join.clone()))
                    }
                },

                Event::Leave(leave) => {
                    for (id, _) in clients.iter() {
                        message_cache.register(id.clone());
                        message_cache.push(&id, Event::Leave(leave.clone()))
                    }
                },

                Event::EntityMove(r#move) => {
                    use crate::events::server::EntityMove;
                    use crate::events::common::EntityKind;

                    let EntityMove { location, .. } = r#move;

                    for (id, _) in clients.iter() {
                        let player = entities.get(&id).ok_or(NoneError)?;

                        if let EntityKind::Player(player) = &player.kind {
                            if player.location.level == location.level {
                                message_cache.register(*id);
                                message_cache.push(&id, Event::EntityMove(r#move.clone()))
                            }
                        }
                    }
                },

                Event::ZoneInfo(zone_info) => {
                    let id = zone_info.receiver;

                    message_cache.register(id);
                    message_cache.push(&id, Event::ZoneInfo(zone_info))
                }
            }
        }

        for (client, events) in message_cache.into_iter() {
            let client = clients.get(&client).ok_or(NoneError)?;

            client.sender.clone().try_send(events)?;
        }

        Ok(())
    }
}
