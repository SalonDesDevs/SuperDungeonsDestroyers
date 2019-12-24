use super::Context;

use std::time::Instant;

use failure::Fallible;

use log::{ warn, debug };

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
        let message_cache = self.context.events().extract();

        if message_cache.is_empty() {
            return Ok(())
        }

        let clients = self.context.clients().read().unwrap();

        for (client, events) in message_cache.into_iter() {
            if let Some(client) = clients.get(&client) {
                client.sender.clone().try_send(events)?;
            }
        }

        Ok(())
    }
}
