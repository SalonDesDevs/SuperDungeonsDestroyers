use crate::utils::WriteToBuilder;
use crate::binding::common;
use crate::network::Tx;

use super::location::Location;

use flatbuffers::{ WIPOffset, FlatBufferBuilder };

use failure::Fallible;

use std::net::SocketAddr;

#[derive(Clone)]
pub struct Player {
    pub name: String,
    pub location: Option<Location>,

    pub tx: Tx,
    pub address: SocketAddr,
}

impl Player {
    pub fn new(address: SocketAddr, tx: Tx) -> Self {
        Player {
            name: ":upside_down:".into(),
            location: None,
            tx,
            address
        }
    }
}

impl<'b> WriteToBuilder<'b, WIPOffset<common::Player<'b>>> for Player {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<WIPOffset<common::Player<'b>>> {
        let Player { name, location, .. } = self;

        let name = builder.create_string(&name);
        let location = location
            .as_ref()
            .map(|location| location.write(builder))
            .transpose()?;

        let player = common::Player::create(
            &mut builder,
            &common::PlayerArgs {
                name: Some(name),
                location: location.as_ref()
            }
        );

        Ok(player)
    }
}

impl<'b> WriteToBuilder<'b, WIPOffset<common::Entity<'b>>> for Player {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<WIPOffset<common::Entity<'b>>> {
        let player: WIPOffset<common::Player> = self.write(&mut builder)?;

        let entity = common::Entity::create(
            &mut builder,
            &common::EntityArgs {
                kind: Some(player.as_union_value()),
                kind_type: common::EntityKind::Player
            }
        );

        Ok(entity)
    }
}
