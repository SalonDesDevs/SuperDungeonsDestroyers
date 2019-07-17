use crate::network::{ ClientMessages, ServerMessages, Peer };
use crate::binding::{ client, server };

use std::io;

use failure::Fallible;

use flatbuffers::{ WIPOffset, FlatBufferBuilder };

pub struct Listener;

type ServerMessage<'b> = WIPOffset<server::Message<'b>>;

impl Listener {
    pub fn handle_messages(peer: &Peer, messages: ClientMessages) -> Fallible<()> {
        let ClientMessages { messages, .. } = messages;
        let messages = messages.messages();

        let mut builder = FlatBufferBuilder::new();
        let mut server_messages = Vec::new();

        for message in 0..messages.len() {
            let message = messages.get(message);

            let result = Listener::handle_message(peer, message, &mut builder)?;
            server_messages.extend(result);
        }

        let server_messages = builder.create_vector(&server_messages);
        let server_messages = server::Messages::create(
            &mut builder,
            &server::MessagesArgs {
                messages: Some(server_messages)
            }
        );

        builder.finish(server_messages, None);

        let server_messages = ServerMessages {
            bytes: builder.finished_data().into()
        };

        peer.tx.clone().try_send(server_messages)?;

        Ok(())
    }

    pub fn handle_message<'b>(_peer: &Peer, message: client::Message, mut builder: &mut FlatBufferBuilder<'b>) -> Fallible<Vec<ServerMessage<'b>>> {
        use client::Content;

        match message.content_type() {
            Content::Ping => {
                let message = message.content_as_ping().unwrap();

                dbg!(message.value());

                let pong = server::Pong::create(
                    &mut builder,
                    &server::PongArgs {
                        value: message.value()
                    }
                );

                let response = server::Message::create(
                    &mut builder,
                    &server::MessageArgs {
                        content: Some(pong.as_union_value()),
                        content_type: server::Content::Pong
                    }
                );

                Ok(vec![response])
            },

            Content::Move => {
                unimplemented!()
            }

            Content::NONE => {
                Err(io::Error::from(io::ErrorKind::InvalidData))?
            }
        }
    }
}
