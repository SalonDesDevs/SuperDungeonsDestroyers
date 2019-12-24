use crate::events::common::{ LevelEnvironment, Coordinates };
use crate::error::NoneError;

use std::sync::{ Arc, RwLock };
use std::collections::HashMap;
use std::path::Path;

use failure::Fallible;

use tiled::PropertyValue;

use log::{ debug };

#[derive(Debug)]
pub struct Map {
    pub inner: tiled::Map,
    static_solid: Vec<Coordinates>,
    spawnpoints: Vec<Coordinates>
}

impl Map {
    pub fn static_solids(&self) -> &Vec<Coordinates> {
        &self.static_solid
    }
    pub fn spawn_points(&self) -> &Vec<Coordinates> { &self.spawnpoints }
    fn new(environment: &LevelEnvironment) -> Fallible<Self> {
        let inner = tiled::parse_file(environment.as_ref())?;
        let static_solid = Map::find_static_solid(&inner)?;
        let spawnpoints = Map::find_spawnpoints(&inner)?;
        let map = Map {
            inner,
            static_solid,
            spawnpoints
        };

        Ok(map)
    }

    fn find_static_solid(map: &tiled::Map) -> Fallible<Vec<Coordinates>> {
        let tiled::Map { tilesets, layers, .. } = map;

        let solid_tileset = tilesets
            .iter()
            .find(|set| set.name == "dungeon_combined")
            .ok_or(failure::err_msg("Can't find the tileset"))?;

        let solid_tileset: Vec<_> = solid_tileset.tiles
            .iter()
            .filter(|tile| tile.properties.get("solid").map(
                |value| value == &PropertyValue::BoolValue(true)).unwrap_or(false)
            )
            .map(|tile| tile.id + solid_tileset.first_gid)
            .collect();

        let tiles = layers
            .iter()
            .flat_map(|layer| layer.tiles.iter().enumerate().flat_map(
                |(y, lines)| lines.iter().enumerate().map(move |(x, tile)| (x, y, tile))
            ))
            .filter(|(_, _, tile)| solid_tileset.contains(tile))
            .map(|(x, y, _)| Coordinates {
                x: x as u8,
                y: y as u8
            })
            .collect();

        Ok(tiles)
    }

    fn find_spawnpoints(map: &tiled::Map) -> Fallible<Vec<Coordinates>> {
        let tiled::Map { object_groups, tile_width, tile_height, .. } = map;

        let group = object_groups
            .iter()
            .find(|group| group.name == "objects")
            .ok_or(NoneError)?;

        let spawnpoints = group.objects
            .iter()
            .filter(|object| object.properties.get("spawn").map(|value| value == &PropertyValue::BoolValue(true)).unwrap_or(false))
            .map(|object| Coordinates {
                x: ((object.x as u32) / *tile_width) as u8,
                y: ((object.y as u32) / *tile_height) as u8,
            })
            .collect();

        Ok(spawnpoints)
    }
}

impl AsRef<Path> for LevelEnvironment {
    fn as_ref(&self) -> &Path {
        let path = match self {
            LevelEnvironment::Bottom => "./commons/rooms/bottom.tmx",
            LevelEnvironment::Cave => "./commons/rooms/cave.tmx",
            LevelEnvironment::Top => "./commons/rooms/top.tmx",
            LevelEnvironment::CollisionsTester => "./commons/rooms/collisions_tester.tmx"
        };

        Path::new(path)
    }
}
#[derive(Debug, Clone)]
pub struct Level {
    level: u8,
    pub map: Arc<Map>
}
impl Level {
    pub fn id(&self) -> u8 {
        self.level
    }
}
#[derive(Default)]
pub struct LevelManager {
    cache: RwLock<HashMap<LevelEnvironment, Arc<Map>>>,
    levels: RwLock<HashMap<u8, Level>>
}

impl LevelManager {

    pub fn level(&self, level_id: u8) -> Option<Level> {
        self.levels.read().unwrap().get(&level_id).cloned()
    }
    fn fetch(&self, environment: &LevelEnvironment) -> Option<Arc<Map>> {
        self.cache.read().unwrap().get(environment).cloned()
    }

    fn load(&self, environment: LevelEnvironment) -> Fallible<Arc<Map>> {
        match self.fetch(&environment) {
            Some(map) => Ok(map),
            None => {
                let map = Arc::new(Map::new(&environment)?);

                self.cache.write().unwrap().insert(environment, map.clone());

                Ok(map)
            }
        }
    }

    pub fn generate_level(&self, level: u8) -> Fallible<()> {
        // TODO: Randomize
        let environment = LevelEnvironment::Top;
        let level = Level {
            level,
            map: self.load(environment)?
        };

        self.levels.write().unwrap().insert(level.level, level);

        Ok(())
    }
}
