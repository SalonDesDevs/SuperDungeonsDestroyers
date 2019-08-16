package org.salondesdevs.superdungeonsdestroyers.server.systems;

import io.netty.channel.ChannelHandlerContext;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NetHandler {
    private static final Logger logger = LoggerFactory.getLogger( NetHandler.class );

    @Inject
    Mapper<PlayerConnection> playerConnectionMapper;

    @Inject
    Synchronizer synchronizer;

    private ChannelHandlerContext ctx;
    private List<Packet> packetsToHandle;

    public void initialize(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.packetsToHandle = new ArrayList<>();
    }

    public void process() {
        synchronized (this.packetsToHandle) {
            this.packetsToHandle.forEach(this::handle);
            this.packetsToHandle.clear();
        }
    }

    public void enqeueForHandling(Packet packet) {
        synchronized (this.packetsToHandle) {
            this.packetsToHandle.add(packet);
        }
    }

    @Inject
    MotionSystem motionSystem;

    @Inject
    EnvironmentManager environmentManager;

    private int playerId;

    private boolean checkedIn = false;

    private void handle(Packet packet) {
        if (packet instanceof VersionCheck) {
            VersionCheck versionCheck = (VersionCheck) packet;
            // TODO: check protocol version
            if (versionCheck.minor != Integer.MIN_VALUE) {
                this.checkedIn = true;
                // If the protocol is correct, we actually spawn the player in the world.
                this.playerId = environmentManager.spawn(EntityKind.PLAYER);
                this.playerConnectionMapper.set(playerId, new PlayerConnection(ctx));
                synchronizer.startSynchronizingWith(this.playerId);
                environmentManager.teleport(playerId, 1, 1);
            }
            else {
                // Otherwise, close.
                ctx.close();
            }
        }

        if (!this.checkedIn) {
            logger.warn("Connection {} tried to bypass version check", ctx.channel().remoteAddress());
            return;
        }
        if (packet instanceof PlayerMove) {
            motionSystem.playerMoved(this.playerId, ((PlayerMove) packet));
        }
    }
}
