use crate::network::{ ClientMessages, ServerMessages, Peer };

use super::shared::{ Player, Shared };

use std::sync::{ Arc, Mutex };

pub struct Listener;

impl Listener {
    pub fn handle_messages(peer: &Peer, messages: ClientMessages) {
        unimplemented!()
    }

    pub fn handle_message() {
        unimplemented!()
    }
}
