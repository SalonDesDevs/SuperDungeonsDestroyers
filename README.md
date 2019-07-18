# Super Dungeons Destroyers

A community made project by SalonDesDevs members.
SDD is a multiplayer dungeon crawler game.

# Running

## client-side:

```
super-dungeons-destroyer-client/gradlew desktop:run
```

## server-side

You need to enable the nightly toolchain in order to run the server side

```console
$ env RUST_LOG=sdd=debug cargo +nightly run
```

You can change the `RUST_LOG` variable to target other logs.
