#[allow(non_snake_case, unused_imports)]
#[path = "../../target/flatbuffers/player_generated.rs"]
mod player_generated;

pub use player_generated::sdd::player;

#[allow(non_snake_case, unused_imports)]
#[path = "../../target/flatbuffers/client_generated.rs"]
mod client_generated;

pub use client_generated::sdd::client;


#[allow(non_snake_case, unused_imports)]
#[path = "../../target/flatbuffers/server_generated.rs"]
mod server_generated;

pub use server_generated::sdd::server;
