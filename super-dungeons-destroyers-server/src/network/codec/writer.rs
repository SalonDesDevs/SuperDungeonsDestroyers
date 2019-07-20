use crate::events;
use crate::binding;

use flatbuffers::{ FlatBufferBuilder, WIPOffset as W };
use failure::Fallible as F;

pub trait FlatWrite<'b, Item> {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> F<Item>;
}

// Server implementations

use binding::server::Welcome as BWelcome;
use events::server::Welcome as EWelcome;

impl<'b> FlatWrite<'b, W<BWelcome<'b>>> for EWelcome {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> F<W<BWelcome<'b>>> {
        let me = self.me.write(&mut builder)?;
        let connect = BWelcome::create(
            &mut builder,
            &binding::server::WelcomeArgs {
                me: Some(me)
            }
        );

        Ok(connect)
    }
}

use binding::server::Join as BJoin;
use events::server::Join as EJoin;

impl<'b> FlatWrite<'b, W<BJoin<'b>>> for EJoin {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> F<W<BJoin<'b>>> {
        let player = self.player.write(&mut builder)?;
        let join = BJoin::create(
            &mut builder,
            &binding::server::JoinArgs {
                player: Some(player)
            }
        );

        Ok(join)
    }
}

use binding::server::Leave as BLeave;
use events::server::Leave as ELeave;

impl<'b> FlatWrite<'b, W<BLeave<'b>>> for ELeave {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> F<W<BLeave<'b>>> {
        let player = self.player.write(&mut builder)?;
        let leave = BLeave::create(
            &mut builder,
            &binding::server::LeaveArgs {
                player: Some(player)
            }
        );

        Ok(leave)
    }
}

use binding::server::ZoneInfo as BZoneInfo;
use events::server::ZoneInfo as EZoneInfo;

impl<'b> FlatWrite<'b, W<BZoneInfo<'b>>> for EZoneInfo {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> F<W<BZoneInfo<'b>>> {
        let environment = self.environment.write(&mut builder)?;
        let entities: Vec<_> = self.entities
            .iter()
            .map(|entity| entity.write(&mut builder))
            .collect::<F<_>>()?;
        let entities = builder.create_vector(&entities);

        let zone_info = BZoneInfo::create(
            &mut builder,
            &binding::server::ZoneInfoArgs {
                level: self.level,
                environment: environment,
                entities: Some(entities)
            }
        );

        Ok(zone_info)
    }
}

use binding::server::EntityMove as BEntityMove;
use events::server::EntityMove as EEntityMove;

impl<'b> FlatWrite<'b, W<BEntityMove<'b>>> for EEntityMove {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> F<W<BEntityMove<'b>>> {
        let location = self.location.write(&mut builder)?;
        let entity_move = BEntityMove::create(
            &mut builder,
            &binding::server::EntityMoveArgs {
                location: Some(&location),
                entity_id: self.entity_id
            }
        );

        Ok(entity_move)
    }
}

use binding::server::{ Event as BEvent, EventUnion as BEventUnion };
use events::server::Event as EEvent;

impl<'b> FlatWrite<'b, W<BEvent<'b>>> for EEvent {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> F<W<BEvent<'b>>> {
        let (event_type, event) = match self {
            EEvent::Welcome(welcome) => (BEventUnion::Welcome, welcome.write(&mut builder)?.as_union_value()),
            EEvent::Join(join) => (BEventUnion::Join, join.write(&mut builder)?.as_union_value()),
            EEvent::Leave(leave) => (BEventUnion::Leave, leave.write(&mut builder)?.as_union_value()),
            EEvent::EntityMove(entity_move) => (BEventUnion::EntityMove, entity_move.write(&mut builder)?.as_union_value()),
            EEvent::ZoneInfo(zone_info) => (BEventUnion::ZoneInfo, zone_info.write(&mut builder)?.as_union_value()),
        };

        let event = BEvent::create(
            &mut builder,
            &binding::server::EventArgs {
                event_type,
                event: Some(event)
            }
        );

        Ok(event)
    }
}

// Common implementations

use binding::common::Direction as BDirection;
use events::common::Direction as EDirection;

impl FlatWrite<'_, BDirection> for EDirection {
    fn write(&self, _: &mut FlatBufferBuilder) -> F<BDirection> {
        let direction = match self {
            EDirection::Up => BDirection::Up,
            EDirection::Down => BDirection::Down,
            EDirection::Right => BDirection::Right,
            EDirection::Left => BDirection::Left
        };

        Ok(direction)
    }
}

use binding::common::Player as BPlayer;
use events::common::Player as EPlayer;

impl<'b> FlatWrite<'b, W<BPlayer<'b>>> for EPlayer {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> F<W<BPlayer<'b>>> {
        let name = builder.create_string(&self.name);
        let location = self.location.write(&mut builder)?;

        let player = BPlayer::create(
            &mut builder,
            &binding::common::PlayerArgs {
                name: Some(name),
                location: Some(&location)
            }
        );

        Ok(player)
    }
}

use binding::common::Location as BLocation;
use events::common::Location as ELocation;

impl FlatWrite<'_, BLocation> for ELocation {
    fn write(&self, _: &mut FlatBufferBuilder) -> F<BLocation> {
        let location = BLocation::new(self.level, self.coordinates.x, self.coordinates.y);

        Ok(location)
    }
}

use binding::common::{ Entity as BEntity, EntityKind as BEntityKind };
use events::common::{ Entity as EEntity, EntityKind as EEntityKind };

impl<'b> FlatWrite<'b, W<BEntity<'b>>> for EEntity {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> F<W<BEntity<'b>>> {
        let (kind_type, kind) = match &self.kind {
            EEntityKind::Player(player) => (BEntityKind::Player, player.write(&mut builder)?.as_union_value())
        };

        let entity = BEntity::create(
            &mut builder,
            &binding::common::EntityArgs {
                entity_id: self.entity_id,
                kind: Some(kind),
                kind_type
            }
        );

        Ok(entity)
    }
}

use binding::common::LevelEnvironment as BLevelEnvironment;
use events::common::LevelEnvironment as ELevelEnvironment;

impl FlatWrite<'_, BLevelEnvironment> for ELevelEnvironment {
    fn write(&self, _: &mut FlatBufferBuilder) -> F<BLevelEnvironment> {
        let environment = match self {
            ELevelEnvironment::Bottom => BLevelEnvironment::Bottom,
            ELevelEnvironment::Cave => BLevelEnvironment::Cave,
            ELevelEnvironment::Top => BLevelEnvironment::Top,
            ELevelEnvironment::CollisionsTester => BLevelEnvironment::CollisionsTester,
        };

        Ok(environment)
    }
}
