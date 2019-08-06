use crate::game::Context;
use crate::network;
use crate::events::client;
use crate::events::common;
use crate::events::server;

use std::cmp::{ min, max };
use failure::Fallible;

use log::{ debug };
pub struct Listener {
    client: network::Client,
}

impl Listener {
    pub fn new(client: network::Client) -> Self {
        Listener {
            client
        }
    }

    fn context(&self) -> &Context {
        &self.client.context
    }

    pub fn process_events(&self, events: Vec<client::Event>) -> Fallible<()> {
        for event in events {
            self.process_event(event)?;
        }

        Ok(())
    }
    //    //         Content::Move => {
//    //             let message = message.content_as_move().unwrap();
//    //             let direction = message.direction();
//
//    //             debug!("{} moved {:?}", peer.address, direction);
//
//    //             let mut players = peer.context.players.write().unwrap();
//    //             let levels = peer.context.levels.read().unwrap();
//
//    //             let player = players.get_mut(&peer.address).unwrap();
//
//    //             let x_move = match direction {
//    //                 common::Direction::Right => 1,
//    //                 common::Direction::Left => -1,
//    //                 _ => 0,
//    //             };
//
//    //             let y_move = match direction {
//    //                 common::Direction::Down => 1,
//    //                 common::Direction::Up => -1,
//    //                 _ => 0,
//    //             };
//
//    //             let current_level = levels.get(player.location.level as usize).unwrap();
//
//    //             let future_location = Location {
//    //                 level: player.location.level,
//    //                 x: min(max(player.location.x as i32 + x_move, 0), (current_level.inner_map.width - 1) as i32) as u8,
//    //                 y: min(max(player.location.y as i32 + y_move, 0), (current_level.inner_map.height - 1) as i32) as u8
//    //             };
//
//    //             let can_move = !current_level.solid_locations.contains(&future_location);
//
//    //             debug!("Move? {:?} {:?} {:?}", player.location, future_location, can_move);
//
//    //             if can_move {
//    //                 player.location = future_location;
//    //             }
//
//    //             Ok(Vec::new())
//    //         },
    fn process_event(&self, event: client::Event) -> Fallible<()> {
        match event {
            client::Event::Move(r#move) => {
                let peer = &self.client.context;
                let players = peer.entities().read().expect("not a valide player");
                let player = players.get(&self.client.id).expect("not a valid user");
                let levels = peer.levels();

                let x_move = match r#move.direction {
                     common::Direction::Right => 1,
                     common::Direction::Left => -1,
                     _ => 0,
                 };

                 let y_move = match r#move.direction {
                     common::Direction::Down => 1,
                     common::Direction::Up => -1,
                     _ => 0,
                 };

                let current_level = &levels.level(player.location().level).unwrap();

                let future_location = common::Location {
                    level: player.location().level,
                    coordinates: common::Coordinates {
                        x:  min(max(player.location().coordinates.x as i32 + x_move, 0), (current_level.map.inner.width - 1) as i32) as u8,
                        y: min(max(player.location().coordinates.y as i32 + y_move, 0), (current_level.map.inner.height - 1) as i32) as u8
                    }
                };
                let can_move = current_level.map.static_solids().contains(&future_location.coordinates);
                debug!("{:?} - Player pos: {:?}", current_level.map.static_solids(), player.location());

                if !can_move {
                    &self.client.context.events().push(self.client.id, server::Event::EntityTeleport(
                        server::EntityTeleport {
                            entity_id: self.client.id,
                            location: *player.location()
                        }

                    ));
                    debug!("Move? {:?} {:?} {:?}", player.location(), future_location, can_move);
                }
                Ok(())

            }
        }
    }

//    // pub fn handle_message<'b>(peer: &Peer, message: client::Message, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<Vec<ServerMessage<'b>>> {
//    //     use client::Content;
//
//    //     match message.content_type() {
//    //         Content::Ping => {
//    //             let message = message.content_as_ping().unwrap();
//
//    //             debug!("{}", message.value());
//
//    //             let pong = server::Pong::create(
//    //                 &mut builder,
//    //                 &server::PongArgs {
//    //                     value: message.value()
//    //                 }
//    //             );
//
//    //             let response = server::Message::create(
//    //                 &mut builder,
//    //                 &server::MessageArgs {
//    //                     content: Some(pong.as_union_value()),
//    //                     content_type: server::Content::Pong
//    //                 }
//    //             );
//
//    //             Ok(vec![response])
//    //         },
//
//    //         Content::Move => {
//    //             let message = message.content_as_move().unwrap();
//    //             let direction = message.direction();
//
//    //             debug!("{} moved {:?}", peer.address, direction);
//
//    //             let mut players = peer.context.players.write().unwrap();
//    //             let levels = peer.context.levels.read().unwrap();
//
//    //             let player = players.get_mut(&peer.address).unwrap();
//
//    //             let x_move = match direction {
//    //                 common::Direction::Right => 1,
//    //                 common::Direction::Left => -1,
//    //                 _ => 0,
//    //             };
//
//    //             let y_move = match direction {
//    //                 common::Direction::Down => 1,
//    //                 common::Direction::Up => -1,
//    //                 _ => 0,
//    //             };
//
//    //             let current_level = levels.get(player.location.level as usize).unwrap();
//
//    //             let future_location = Location {
//    //                 level: player.location.level,
//    //                 x: min(max(player.location.x as i32 + x_move, 0), (current_level.inner_map.width - 1) as i32) as u8,
//    //                 y: min(max(player.location.y as i32 + y_move, 0), (current_level.inner_map.height - 1) as i32) as u8
//    //             };
//
//    //             let can_move = !current_level.solid_locations.contains(&future_location);
//
//    //             debug!("Move? {:?} {:?} {:?}", player.location, future_location, can_move);
//
//    //             if can_move {
//    //                 player.location = future_location;
//    //             }
//
//    //             Ok(Vec::new())
//    //         },
//
//    //         Content::NONE => {
//    //             Err(io::Error::from(io::ErrorKind::InvalidData))?
//    //         }
//    //     }
//    // }
}
