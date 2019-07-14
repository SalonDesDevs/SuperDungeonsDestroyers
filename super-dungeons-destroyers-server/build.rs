use std::path::Path;
use std::io::Result;
use flatc_rust::{ run, Args };

fn main() -> Result<()> {
    println!("cargo:rerun-if-changed=../schemas/player.fbs");
    println!("cargo:rerun-if-changed=../schemas/request.fbs");

    run(Args {
        inputs: &[
            Path::new("../schemas/player.fbs"),
            Path::new("../schemas/request.fbs"),
        ],
        out_dir: &Path::new("../target/flatbuffers"),
        ..Default::default()
    })
}
