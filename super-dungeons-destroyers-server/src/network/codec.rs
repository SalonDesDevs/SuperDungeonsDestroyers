use crate::binding::client;

use super::{ ClientMessages, ServerMessages };

use tokio::codec::{ Encoder, Decoder };
use bytes::BytesMut;

use std::{ io, mem };
use std::convert::TryInto;

use flatbuffers::get_root;

#[derive(Default)]
pub struct MessageCodec {
    size: Option<u32>,
}

impl Decoder for MessageCodec {
    type Item = ClientMessages;
    type Error = io::Error;

    fn decode(&mut self, src: &mut BytesMut) -> io::Result<Option<Self::Item>> {
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
            let messages = unsafe { mem::transmute(get_root::<client::Messages>(&bytes)) };
            let messages = ClientMessages { _bytes: bytes, messages };

            self.size = None;
            Ok(Some(messages))
        }
    }
}

impl Encoder for MessageCodec {
    type Item = ServerMessages;
    type Error = io::Error;

    fn encode(&mut self, response: Self::Item, dst: &mut BytesMut) -> io::Result<()> {
        let length: u32 = response.bytes.len().try_into().unwrap();
        let length = length.to_le_bytes();

        dst.extend(&length);
        dst.extend(response.bytes);

        Ok(())
    }
}
