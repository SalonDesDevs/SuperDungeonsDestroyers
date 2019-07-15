#[allow(non_snake_case, unused_imports)]
#[path = "../../target/flatbuffers/schema_generated.rs"]
pub mod inner;

pub use inner::sdd::{ mob, dungeon, pickable, client, server, player };
