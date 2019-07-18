mod writer;
mod reader;

use crate::events;
use crate::binding;

use self::writer::FlatWrite;
use self::reader::FlatRead;

use tokio::codec::{ Encoder, Decoder };
use bytes::BytesMut;

use std::{ io, mem };
use std::convert::TryInto;

use flatbuffers::{ FlatBufferBuilder, get_root, WIPOffset as W };

use failure::{ Fallible, Error };

#[derive(Default)]
pub struct MessageCodec {
    size: Option<u32>,
}

impl Decoder for MessageCodec {
    type Item = Vec<events::client::Event>;
    type Error = Error;

    fn decode(&mut self, src: &mut BytesMut) -> Fallible<Option<Self::Item>> {
        if self.size.is_none() {
            if src.len() < mem::size_of::<u32>() {
                return Ok(None);
            } else {
                let bytes = src.split_to(mem::size_of::<u32>());
                let size = bytes.as_ref().try_into().unwrap();
                let size = u32::from_le_bytes(size);

                self.size = Some(size);
            }
        }

        let size = self.size.unwrap() as usize;

        if src.len() < size {
            Ok(None)
        } else {
            let bytes = src.split_to(size).freeze();
            let events = get_root::<binding::client::Events>(&bytes).events();
            let events = (0..events.len())
                .map(|event| events.get(event))
                .map(|event| events::client::Event::read(event))
                .collect::<Fallible<_>>()?;

            self.size = None;
            Ok(Some(events))
        }
    }
}

impl Encoder for MessageCodec {
    type Item = Vec<events::server::Event>;
    type Error = Error;

    fn encode(&mut self, events: Self::Item, dst: &mut BytesMut) -> Fallible<()> {
        let mut builder = FlatBufferBuilder::new();
        let events: Vec<W<binding::server::Event>> = events
            .into_iter()
            .map(|event| event.write(&mut builder))
            .collect::<Fallible<_>>()?;

        let events = builder.create_vector(&events);

        let events = binding::server::Events::create(
            &mut builder,
            &binding::server::EventsArgs {
                events: Some(events)
            }
        );

        builder.finish_minimal(events);

        let events = builder.finished_data();
        let length: u32 = events.len().try_into()?;

        dst.extend(&length.to_le_bytes());
        dst.extend(events);

        Ok(())
    }
}
