use std::path::Path;
use std::io::Result;
use flatc_rust::{ run, Args };

fn main() -> Result<()> {
    run(Args {
        inputs: &[
            Path::new("../schemas/player.fbs"),
        ],
        out_dir: &Path::new("../target/flatbuffers"),
        ..Default::default()
    })
}
