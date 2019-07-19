mod id_counter;
mod registration;

use crate::events::common::{ Entity, EntityKind, EntityId, Player, Location };
use crate::events::server;
use crate::network;

use std::sync::{ Mutex, Arc, RwLock };
use std::collections::HashMap;

use failure::Fallible;

#[derive(Default)]
struct InnerContext {
    id_counter: Mutex<EntityId>,
    entities: RwLock<HashMap<EntityId, Entity>>,
    events: RwLock<Vec<server::Event>>,
    clients: RwLock<HashMap<EntityId, network::Client>>
}

#[derive(Clone)]
pub struct Context(Arc<InnerContext>);

impl Context {
    pub fn new() -> Self {
        Context(Arc::new(InnerContext::default()))
    }

}
