use crate::binding::common;
use crate::utils::WriteToBuilder;

use super::{ Shared, Location };

use std::sync::Arc;
use std::path::Path;

use failure::Fallible;

use flatbuffers::{ FlatBufferBuilder, WIPOffset };

use tiled::{ Map, PropertyValue };

#[derive(Clone)]
pub enum LevelKind {
    Bottom,
    Cave,
    Top
}

pub struct Level {
    pub level: u8,
    pub kind: LevelKind,
    pub spawnpoints: Vec<Location>,

    _map: Map,
    _shared: Arc<Shared>
}

impl AsRef<Path> for LevelKind {
    fn as_ref(&self) -> &Path {
        let path = match self {
            LevelKind::Bottom => "../../../../commons/rooms/bottom.tmx",
            LevelKind::Cave => "../../../../commons/rooms/cave.tmx",
            LevelKind::Top => "../../../../commons/rooms/top.tmx",
        };

        Path::new(path)
    }
}

impl Level {
    pub fn new(level: u8, kind: LevelKind, shared: Arc<Shared>) -> Fallible<Self> {
        let map = tiled::parse_file(kind.as_ref())?;
        let spawnpoints = Level::spawnpoints(level, &map)?;

        let level = Level {
            level,
            kind,
            spawnpoints,

            _map: map,
            _shared: shared
        };

        Ok(level)
    }

    fn spawnpoints(level: u8, map: &Map) -> Fallible<Vec<Location>> {
        let Map { object_groups, tile_width, tile_height, .. } = map;

        let group = object_groups
            .iter()
            .find(|group| group.name == "objects")
            .ok_or(failure::err_msg("Missing objects in map"))?;

        let spawnpoints = group.objects
            .iter()
            .filter(|object| object.properties.get("spawn").map(|value| value == &PropertyValue::BoolValue(true)).unwrap_or(false))
            .map(|object| Location {
                level,
                x: ((object.x as u32) / tile_width) as u8,
                y: ((object.y as u32) / tile_height) as u8,
            })
            .collect();

        Ok(spawnpoints)
    }
}

impl Into<common::LevelKind> for LevelKind {
    fn into(self) -> common::LevelKind {
        match self {
            LevelKind::Bottom => common::LevelKind::Bottom,
            LevelKind::Cave => common::LevelKind::Cave,
            LevelKind::Top => common::LevelKind::Top
        }
    }
}

impl<'b> WriteToBuilder<'b, WIPOffset<common::Level<'b>>> for Level {
    fn write(&self, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<WIPOffset<common::Level<'b>>> {
        let room = common::Level::create(
            &mut builder,
            &common::LevelArgs {
                kind: self.kind.clone().into()
            }
        );

        Ok(room)
    }
}
