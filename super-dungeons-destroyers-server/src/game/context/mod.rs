mod id_counter;
mod registration;

use crate::events::common::{ Entity, EntityId };
use crate::events::server;
use crate::network;

use std::sync::{ Mutex, Arc, RwLock, RwLockReadGuard };
use std::collections::HashMap;

type Clients = HashMap<EntityId, network::Client>;
type Entities = HashMap<EntityId, Entity>;

#[derive(Default)]
struct InnerContext {
    id_counter: Mutex<EntityId>,
    entities: RwLock<Entities>,
    events: RwLock<Vec<server::Event>>,
    clients: RwLock<Clients>
}

#[derive(Clone)]
pub struct Context(Arc<InnerContext>);

impl Context {
    pub fn new() -> Self {
        Context(Arc::new(InnerContext::default()))
    }

    pub fn consume_events(&self) -> Vec<server::Event> {
        self.0.events.write().unwrap().drain(..).collect()
    }

    pub fn clients(&self) -> RwLockReadGuard<Clients> {
        self.0.clients.read().unwrap()
    }

    pub fn entities(&self) -> RwLockReadGuard<Entities> {
        self.0.entities.read().unwrap()
    }
}
