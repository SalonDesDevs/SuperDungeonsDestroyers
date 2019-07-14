#[allow(non_snake_case)]
#[path = "../../target/flatbuffers/player_generated.rs"]
mod player_generated;

pub use player_generated::sdd::player as player;

#[allow(non_snake_case)]
#[path = "../../target/flatbuffers/request_generated.rs"]
mod request_generated;

pub use request_generated::sdd::request as request;
