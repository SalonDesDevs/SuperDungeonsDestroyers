use super::shared::Shared;

use failure::Fallible;

use std::sync::{ Arc, Mutex };

pub struct GameLoop;

impl GameLoop {
    pub fn tick(_shared: Arc<Mutex<Shared>>) -> Fallible<()> {
        eprintln!("Game ticked!");

        Ok(())
    }
}
