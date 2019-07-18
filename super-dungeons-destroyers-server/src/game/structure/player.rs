use crate::utils::WriteToBuilder;
use crate::binding::common;
use crate::network::Tx;

use super::{ Context, Location, ServerMessages };

use flatbuffers::{ WIPOffset, FlatBufferBuilder };

use failure::Fallible;

use std::net::SocketAddr;
use std::sync::Arc;

#[derive(Clone)]
pub struct Player {
    pub name: String,
    pub location: Location,
    pub entity_id: u64,

    pub tx: Tx,
    pub address: SocketAddr,
    pub context: Arc<Context>
}

impl Player {
    pub fn new(address: SocketAddr, tx: Tx, context: Arc<Context>) -> Fallible<Self> {
        let entity_id = context.next_entity_id();
        let location = *context.levels.read().unwrap().get(0).unwrap().spawnpoints.get(0).unwrap();
        let player = Player {
            name: ":upside_down:".into(),
            location,
            tx,
            address,
            context,
            entity_id
        };

        Ok(player)
    }

    pub fn send(&self, message: ServerMessages) -> Fallible<()> {
        Ok(self.tx.clone().try_send(message)?)
    }
}

impl<'b> WriteToBuilder<'b, WIPOffset<common::Player<'b>>> for Player {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<WIPOffset<common::Player<'b>>> {
        let Player { name, location, .. } = self;

        let name = builder.create_string(&name);
        let location = location.write(builder)?;

        let player = common::Player::create(
            &mut builder,
            &common::PlayerArgs {
                name: Some(name),
                location: Some(&location)
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
                entity_id: self.entity_id,
                kind: Some(player.as_union_value()),
                kind_type: common::EntityKind::Player
            }
        );

        Ok(entity)
    }
}
