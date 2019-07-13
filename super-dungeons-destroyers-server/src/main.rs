#![feature(async_await)]
#![deny(warnings, rust_2018_idioms)]
use sdd::player::{ get_root_as_player, Player, PlayerArgs, Vec3 };
use flatbuffers::FlatBufferBuilder;
use tokio;
use tokio::io::AsyncWriteExt;
use tokio::net::TcpListener;
#[tokio::main]
async fn main() {
    let mut builder = FlatBufferBuilder::new();
    let addr = "127.0.0.1:9000".parse().unwrap();
    let listener = TcpListener::bind(&addr).unwrap();
    let name = builder.create_string("John Doe");

    let player = Player::create(
        &mut builder,
        &PlayerArgs {
            name: Some(name),
            location: Some(&Vec3::new(0f32, 0f32, 0f32))
        }
    );

    builder.finish(player, None);
    
    let buffer = builder.finished_data();
    let player = get_root_as_player(buffer);

    println!("Player's name is {}", player.name());
}
