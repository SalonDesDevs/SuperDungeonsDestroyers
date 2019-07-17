use flatbuffers::FlatBufferBuilder;

use failure::Fallible;

pub trait WriteToBuilder<'b, Item> {
    fn write(&self, builder: &mut FlatBufferBuilder<'b>) -> Fallible<Item>;
}
