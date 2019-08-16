package org.salondesdevs.superdungeonsdestroyers.server.systems;

import io.netty.channel.ChannelHandlerContext;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.Welcome;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NetHandler {
    @Inject
    World world;

    @Inject
    Mapper<PlayerConnection> playerConnectionMapper;

    @Inject
    EnvironmentTracker environmentTracker;

    @Inject
    EntityCreator entityCreator;

    private int playerId;
    private PlayerConnection playerConnection;
    private List<Packet> packetsToHandle;

    public void initialize(ChannelHandlerContext ctx) {
        this.playerId = entityCreator.createPlayer();
        this.playerConnection = new PlayerConnection(ctx);
        playerConnectionMapper.set(playerId, playerConnection);
        this.packetsToHandle = new ArrayList<>();
    }

    public void process() {
        synchronized (this.packetsToHandle) {
            this.packetsToHandle.forEach(this::handle);
            this.packetsToHandle.clear();
        }
    }

    public void enqeue(Packet packet) {
        synchronized (this.packetsToHandle) {
            this.packetsToHandle.add(packet);
        }
    }

    public PlayerConnection getPlayerConnection() {
        return playerConnection;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Inject
    MotionSystem motionSystem;

    private void handle(Packet packet) {
        if (packet instanceof VersionCheck) {
            // TODO: check protocol version
            this.environmentTracker.onPlayerJoin(this.playerId);
        }
        if (packet instanceof PlayerMove) {
            motionSystem.playerMoved(this.playerId, ((PlayerMove) packet));
        }
    }
}
