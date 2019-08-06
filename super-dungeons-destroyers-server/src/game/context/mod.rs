mod id_counter;
mod registration;
mod level_manager;
mod event_cache;

use crate::events::common::{ Entity, EntityId };
use crate::network;

use self::level_manager::LevelManager;
use self::event_cache::EventCache;

use std::sync::{ Mutex, Arc, RwLock };
use std::collections::HashMap;

use failure::Fallible;

type Clients = HashMap<EntityId, network::Client>;
type Entities = HashMap<EntityId, Entity>;

#[derive(Default)]
struct InnerContext {
    id_counter: Mutex<EntityId>,
    entities: RwLock<Entities>,
    clients: RwLock<Clients>,
    levels: LevelManager,
    events: EventCache
}

#[derive(Clone)]
pub struct Context(Arc<InnerContext>);

impl Context {
    pub fn new() -> Fallible<Self> {
        let context = Context(Arc::new(InnerContext::default()));

        context.0.levels.generate_level(0)?;

        Ok(context)
    }

    pub fn clients(&self) -> &RwLock<Clients> {
        &self.0.clients
    }
    pub fn levels(&self) -> &LevelManager {
        &self.0.levels
    }
    pub fn entities(&self) -> &RwLock<Entities> {
        &self.0.entities
    }

    pub fn events(&self) -> &EventCache {
        &self.0.events
    }
}
