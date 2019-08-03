use crate::events::common::EntityId;
use crate::events::server::Event;

use std::sync::RwLock;
use std::collections::HashMap;
use std::mem;

#[derive(Default)]
pub struct EventCache {
    inner: RwLock<HashMap<EntityId, Vec<Event>>>
}

impl EventCache {
    pub fn push(&self, target: EntityId, event: Event) {
        let mut inner = self.inner.write().unwrap();

        if !inner.contains_key(&target) {
            inner.insert(target, vec![event]);
        } else {
            let target = inner.get_mut(&target).unwrap();
            target.push(event);
        }
    }

    pub fn extract(&self) -> HashMap<EntityId, Vec<Event>> {
        let mut inner = self.inner.write().unwrap();
        let mut result = HashMap::new();

        mem::swap(&mut *inner, &mut result);

        result
    }
}
