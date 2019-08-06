use crate::events::common::{ Entity, EntityKind, Player, Location, EntityId, Coordinates };
use crate::events::server;
use crate::error::NoneError;
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

    fn register_event(&self, event: server::Event) -> Fallible<()> {
        let clients = self.clients().read().unwrap();
        let entities = self.entities().read().unwrap();
        let events = self.events();

        match event {
            server::Event::Welcome(welcome) => {
                let id = welcome.me.entity_id.clone();
                events.push(id, server::Event::Welcome(welcome));
                let player = entities.get(&id).expect("player not found");
                let level = self.levels().level(0).expect("level not found");
                let spawnpoints = level.map.spawn_points();
                let spawn_player = server::Event::EntityTeleport(
                    server::EntityTeleport {
                        entity_id: id,
                        location: Location {
                            level: level.id(),
                            coordinates: *spawnpoints.first().expect("no spawn points found")
                        }
                    }
                );
                events.push(id, spawn_player)
            },

            server::Event::Join(join) => {
                for (id, _) in clients.iter() {
                    events.push(*id, server::Event::Join(join.clone()));

                }
            },

            server::Event::Leave(leave) => {
                for (id, _) in clients.iter() {
                    events.push(*id, server::Event::Leave(leave.clone()))
                }
            },
            server::Event::EntityMove(r#move) => {
                unimplemented!()
            }
            server::Event::EntityTeleport(r#move) => {
                use crate::events::server::EntityTeleport;

                let EntityTeleport { location, .. } = r#move;

                for (id, _) in clients.iter() {
                    let player = entities.get(id).ok_or(NoneError)?;

                    if player.location().level == location.level {
                        events.push(*id, server::Event::EntityTeleport(r#move.clone()))
                    }
                }
            },

            server::Event::ZoneInfo(zone_info) => {
                events.push(zone_info.receiver, server::Event::ZoneInfo(zone_info))
            }
        }

        Ok(())
    }

    fn create_player(&self, entity_id: EntityId) -> Fallible<Entity> {
        let level = self.levels().level(0).expect("level not found");
        let spawnpoints = level.map.spawn_points();
        let player = Player {
            name: ":upside_down:".to_string(),
            // TODO: Get a spawnpoint from the first level.
            location: Location {
                level: 0,
                coordinates: *spawnpoints.first().expect("not spawn points found")
            }
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
        self.register_event(event)?;

        Ok(entity)
    }

    pub fn register_client(&self, client: network::Client) -> Fallible<()> {
        let me = self.create_player(client.id)?;
        let event = server::Event::Welcome(server::Welcome { me });

        self.register_event(event)?;
        self.0.clients.write().unwrap().insert(client.id, client);

        Ok(())
    }

    pub fn disconnection(&self, _: SocketAddr) {
        unimplemented!()
    }
}
