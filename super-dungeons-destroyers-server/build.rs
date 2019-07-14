use std::path::Path;
use std::io::Result;
use flatc_rust::{ run, Args };

fn main() -> Result<()> {
    println!("cargo:rerun-if-changed=../schemas/player.fbs");

    println!("cargo:rerun-if-changed=../schemas/client.fbs");
    println!("cargo:rerun-if-changed=../schemas/server.fbs");

    run(Args {
        inputs: &[
            Path::new("../schemas/player.fbs"),

            Path::new("../schemas/client.fbs"),
            Path::new("../schemas/server.fbs"),
        ],
        out_dir: &Path::new("../target/flatbuffers"),
        ..Default::default()
    })
}
