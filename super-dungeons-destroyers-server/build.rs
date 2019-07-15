use std::path::Path;
use std::io::Result;
use flatc_rust::{ run, Args };

fn main() -> Result<()> {
    println!("cargo:rerun-if-changed=../commons/schemas/player.fbs");

    println!("cargo:rerun-if-changed=../commons/schemas/client.fbs");
    println!("cargo:rerun-if-changed=../commons/schemas/server.fbs");

    run(Args {
        inputs: &[
            Path::new("../commons/schemas/player.fbs"),

            Path::new("../commons/schemas/client.fbs"),
            Path::new("../commons/schemas/server.fbs"),
        ],
        out_dir: &Path::new("../target/flatbuffers"),
        ..Default::default()
    })
}
