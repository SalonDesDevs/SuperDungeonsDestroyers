use sdd::player::{ get_root_as_player, Player, PlayerArgs, Vec3 };
use flatbuffers::FlatBufferBuilder;

fn main() {
    let mut builder = FlatBufferBuilder::new();

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

    println!("Player's name is {:?}", player.name());
}
