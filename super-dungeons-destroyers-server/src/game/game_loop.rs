use super::shared::Shared;

use crate::binding::{ server, common };
use crate::network::ServerMessages;

use failure::Fallible;

use std::sync::{ Arc, Mutex };

use flatbuffers::FlatBufferBuilder;

pub struct GameLoop;

impl GameLoop {
    pub fn tick(shared: Arc<Mutex<Shared>>) -> Fallible<()> {
        eprintln!("Game ticked!");

        let mut builder = FlatBufferBuilder::new();

        let room = common::Room::create(
            &mut builder,
            &common::RoomArgs {
                kind: common::RoomKind::TestMap
            }
        );

        let environment = server::Environment::create(
            &mut builder,
            &server::EnvironmentArgs {
                room: Some(room)
            }
        );

        let message = server::Message::create(
            &mut builder,
            &server::MessageArgs {
                content: Some(environment.as_union_value()),
                content_type: server::Content::Pong
            }
        );

        let messages = builder.create_vector(&[message]);

        let messages = server::Messages::create(
            &mut builder,
            &server::MessagesArgs {
                messages: Some(messages)
            }
        );

        builder.finish(messages, None);

        let messages = ServerMessages {
            bytes: builder.finished_data().into()
        };

        shared.lock().unwrap().broadcast(messages)?;

        Ok(())
    }
}
