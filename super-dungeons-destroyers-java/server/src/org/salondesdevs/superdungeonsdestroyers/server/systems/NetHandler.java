package org.salondesdevs.superdungeonsdestroyers.server.systems;

import io.netty.channel.ChannelHandlerContext;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.library.components.*;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.FromClientChat;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerMove;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.PlayerName;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.DisconnectReason;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.VersionCheckSuccess;
import org.salondesdevs.superdungeonsdestroyers.library.utils.ProtocolVersion;
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

    @Inject
    Mapper<MaxHealth> maxHealthMapper;

    @Inject
    Mapper<Size> sizeMapper;

    @Inject
    Mapper<Speed> speedMapper;

    @Inject
    ChatSystem chatSystem;

    @Inject
    Mapper<Name> nameMapper;

    private void handle(Packet packet) {
        if (packet instanceof VersionCheck) {
            VersionCheck versionCheck = (VersionCheck) packet;
            if (versionCheck.major == ProtocolVersion.MAJOR && versionCheck.minor == ProtocolVersion.MINOR) {
                this.checkedIn = true;
                ctx.writeAndFlush(new VersionCheckSuccess());
                // If the protocol is correct, we actually spawn the player in the world.
                this.playerId = environmentManager.spawn(EntityKind.PLAYER);
                this.playerConnectionMapper.set(playerId, new PlayerConnection(ctx));
                synchronizer.startSynchronizingWith(this.playerId);
                environmentManager.teleport(playerId, 1, 1);
                maxHealthMapper.set(playerId, new MaxHealth(382));
                sizeMapper.set(playerId, new Size(1.0f, 1.3f));
                speedMapper.set(playerId, new Speed());
            }
            else {
                // Otherwise, close.
                this.disconnect("Invalid protocol version (expected " + ProtocolVersion.string() + " and got " + ProtocolVersion.toString(versionCheck) + ")");
                return;
            }
        }

        if (!this.checkedIn) {
            logger.error("Connection {} tried to bypass version check", ctx.channel().remoteAddress());
            this.disconnect("You tried to bypass version check");
            return;
        }

        if (packet instanceof PlayerMove) {
            motionSystem.playerMoved(this.playerId, ((PlayerMove) packet));
        }
        else if (packet instanceof FromClientChat) {
            chatSystem.playerChatted(this.playerId, ((FromClientChat) packet));
        }
        else if (packet instanceof PlayerName) {
            nameMapper.set(playerId, new Name(((PlayerName) packet).getName()));
        }
    }

    public void disconnect(String reason) {
        ctx.writeAndFlush(new DisconnectReason(reason));
        ctx.close();
    }
}
