use tokio::codec::{ Decoder, Encoder };
use bytes::{ Bytes, BytesMut, BufMut };
use failure::Error;
use std::mem;
use std::convert::TryInto;
use sdd::request::{ get_root_as_request, Request };

pub struct RequestData {
    _raw: Bytes,
    pub request: Request<'static>,
}

pub struct Codec {
    length: Option<u32>,
}

impl Decoder for Codec {
    type Item = RequestData;
    type Error = Error;

    fn decode(&mut self, src: &mut BytesMut) -> Result<Option<Self::Item>, Self::Error> {
        if self.length.is_none() {
            if src.len() < mem::size_of::<u32>() {
                return Ok(None);
            } else {
                let bytes = src.split_off(mem::size_of::<u32>());
                let length = bytes.as_ref().try_into().unwrap();
                let length = u32::from_le_bytes(length);

                self.length = Some(length);
            }
        }

        let length = self.length.unwrap() as usize;

        if src.len() < length {
            return Ok(None);
        }

        let bytes = src.split_off(length).freeze();
        let request = unsafe { mem::transmute(get_root_as_request(&bytes)) };

        self.length = None;
        Ok(Some(RequestData { _raw: bytes, request }))
    }
}

impl Encoder for Codec {
    type Item = Bytes;
    type Error = Error;

    fn encode(&mut self, response: Self::Item, dst: &mut BytesMut) -> Result<(), Self::Error> {
        dst.put_u32_le(response.len().try_into().unwrap());
        dst.put(response);

        Ok(())
    }
}
