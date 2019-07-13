#[allow(non_snake_case)]
#[path = "../../target/flatbuffers/player_generated.rs"]
mod player_generated;

pub use player_generated::sdd::player as player;
