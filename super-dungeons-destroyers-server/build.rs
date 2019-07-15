use std::path::Path;
use std::io::Result;
use flatc_rust::{ run, Args };

fn main() -> Result<()> {
    println!("cargo:rerun-if-changed=../commons/schemas/schema.fbs");

    run(Args {
        inputs: &[
            Path::new("../commons/schemas/schema.fbs"),
        ],
        out_dir: &Path::new("../target/flatbuffers"),
        ..Default::default()
    })
}
