use crate::events::common::{ Entity, EntityKind, Player, Location, EntityId };
use crate::events::server;
use crate::network;

use super::Context;

use std::net::SocketAddr;

use failure::Fallible;

impl Context {
    fn register_entity(&self, entity: Entity) {
        self.0.entities.write()
            .unwrap()
            .insert(entity.entity_id, entity);
    }

    fn register_event(&self, event: server::Event) {
        self.0.events.write()
            .unwrap()
            .push(event);
    }

    fn create_player(&self, entity_id: EntityId) -> Entity {
        // TODO: Get a spawnpoint from the first level.
        let location = Location {
            level: 0,
            x: 0,
            y: 0
        };

        let player = Player {
            name: ":upside_down:".to_string(),
            location
        };

        let entity = Entity {
            kind: EntityKind::Player(player),
            entity_id
        };

        let join = server::Join {
            player: entity.clone(),
        };

        let event = server::Event::Join(join);

        self.register_entity(entity.clone());
        self.register_event(event);

        entity
    }

    pub fn register_client(&self, client: network::Client) -> Fallible<()> {
        let me = self.create_player(client.id);
        let event = server::Event::Welcome(server::Welcome { me });

        self.register_event(event);
        self.0.clients.write().unwrap().insert(client.id, client);

        Ok(())
    }

    pub fn disconnection(&self, _: SocketAddr) {
        unimplemented!()
    }
}
