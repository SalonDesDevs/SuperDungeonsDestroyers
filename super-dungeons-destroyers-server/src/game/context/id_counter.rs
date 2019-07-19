use crate::events::common::EntityId;

use super::Context;

impl Context {
    pub fn create_entity_id(&self) -> EntityId {
        let mut counter = *self.0.id_counter.lock().unwrap();
        counter += 1;
        counter - 1
    }
}
