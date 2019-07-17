# Super Dungeons Destroyers

A community made project by SalonDesDevs members.
SDD is a multiplayer dungeon crawler game.

# Running

* client-side:

```
super-dungeons-destroyer-client/gradlew desktop:run
```

* server-side
```
cargo +nightly run
```
if you want to use the logging facilities,
you need to set the `RUST_LOG` variable before cargo run.
## example
```
RUST_LOG=info cargo +nightly run
```
this example will only display the info logs.

