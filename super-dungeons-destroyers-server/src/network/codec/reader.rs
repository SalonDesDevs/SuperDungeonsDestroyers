use crate::events;
use crate::binding;
use crate::error::{ NoneError, UnionNoneError, CorruptedError };

use failure::Fallible;

pub trait FlatRead<'b, Item> where Self: Sized {
    fn read(item: Item) -> Fallible<Self>;
}

// Client implementations

use binding::client::Move as BMove;
use events::client::Move as EMove;

impl<'b> FlatRead<'b, BMove<'b>> for EMove {
    fn read(item: BMove<'b>) -> Fallible<Self> {
        let direction = EDirection::read(item.direction())?;
        let move_event = EMove {
            direction
        };

        Ok(move_event)
    }
}

use binding::client::{ Event as BEvent, EventUnion as BEventUnion };
use events::client::Event as EEvent;

impl<'b> FlatRead<'b, BEvent<'b>> for EEvent {
    fn read(item: BEvent<'b>) -> Fallible<Self> {
        let event = match item.event_type() {
            BEventUnion::Move => EEvent::Move(EMove::read(item.event_as_move().ok_or(NoneError)?)?),
            BEventUnion::NONE => Err(UnionNoneError)?,
        };

        Ok(event)
    }
}

// Common implementations

use binding::common::Direction as BDirection;
use events::common::Direction as EDirection;

impl FlatRead<'_, BDirection> for EDirection {
    #[allow(unreachable_patterns)]
    fn read(item: BDirection) -> Fallible<Self> {
        let direction = match item {
            BDirection::Right => EDirection::Right,
            BDirection::Left => EDirection::Left,
            BDirection::Up => EDirection::Up,
            BDirection::Down => EDirection::Down,
            _ => Err(CorruptedError)?
        };

        Ok(direction)
    }
}

use binding::common::Player as BPlayer;
use events::common::Player as EPlayer;

impl<'b> FlatRead<'b, BPlayer<'b>> for EPlayer {
    fn read(item: BPlayer<'b>) -> Fallible<Self> {
        let name = item.name().ok_or(NoneError)?.to_string();
        let location = ELocation::read(*item.location().ok_or(NoneError)?)?;

        let player = EPlayer {
            name,
            location
        };

        Ok(player)
    }
}

use binding::common::Location as BLocation;
use events::common::{ Location as ELocation, Coordinates as ECoordinates };

impl FlatRead<'_, BLocation> for ELocation {
    fn read(item: BLocation) -> Fallible<Self> {
        let location = ELocation {
            level: item.level(),
            coordinates: ECoordinates {
                x: item.x(),
                y: item.y()
            }
        };

        Ok(location)
    }
}

use binding::common::{ Entity as BEntity, EntityKind as BEntityKind };
use events::common::{ Entity as EEntity, EntityKind as EEntityKind };

impl<'b> FlatRead<'b, BEntity<'b>> for EEntity {
    fn read(item: BEntity<'b>) -> Fallible<Self> {
        let kind = match item.kind_type() {
            BEntityKind::Player => EEntityKind::Player(EPlayer::read(item.kind_as_player().ok_or(NoneError)?)?),
            BEntityKind::NONE => Err(UnionNoneError)?
        };

        let entity = EEntity {
            entity_id: item.entity_id(),
            kind
        };

        Ok(entity)
    }
}

use binding::common::LevelEnvironment as BLevelEnvironment;
use events::common::LevelEnvironment as ELevelEnvironment;

impl FlatRead<'_, BLevelEnvironment> for ELevelEnvironment {
    #[allow(unreachable_patterns)]
    fn read(item: BLevelEnvironment) -> Fallible<Self> {
        let environment = match item {
            BLevelEnvironment::Bottom => ELevelEnvironment::Bottom,
            BLevelEnvironment::Cave => ELevelEnvironment::Cave,
            BLevelEnvironment::Top => ELevelEnvironment::Top,
            BLevelEnvironment::CollisionsTester => ELevelEnvironment::CollisionsTester,
            _ => Err(CorruptedError)?
        };

        Ok(environment)
    }
}
