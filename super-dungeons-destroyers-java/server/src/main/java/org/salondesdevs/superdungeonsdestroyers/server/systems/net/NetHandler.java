package org.salondesdevs.superdungeonsdestroyers.server.systems.net;

import io.netty.channel.ChannelHandlerContext;
import net.wytrem.ecs.Mapper;
import org.salondesdevs.superdungeonsdestroyers.library.components.EntityKind;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.VersionCheck;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.DisconnectReason;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.VersionCheckSuccess;
import org.salondesdevs.superdungeonsdestroyers.library.systems.EventBus;
import org.salondesdevs.superdungeonsdestroyers.library.utils.ProtocolVersion;
import org.salondesdevs.superdungeonsdestroyers.server.components.PlayerConnection;
import org.salondesdevs.superdungeonsdestroyers.server.events.PacketReceivedEvent;
import org.salondesdevs.superdungeonsdestroyers.server.events.PlayerJoinedEvent;
import org.salondesdevs.superdungeonsdestroyers.server.systems.EnvironmentManager;
import org.salondesdevs.superdungeonsdestroyers.server.systems.MotionSystem;
import org.salondesdevs.superdungeonsdestroyers.server.systems.Synchronizer;
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
    EventBus eventBus;

    private void handle(Packet packet) {
        if (packet instanceof VersionCheck) {
            VersionCheck versionCheck = (VersionCheck) packet;
            if (versionCheck.major == ProtocolVersion.MAJOR && versionCheck.minor == ProtocolVersion.MINOR) {
                this.checkedIn = true;
                ctx.writeAndFlush(new VersionCheckSuccess());
                // If the protocol is correct, we actually spawn the player in the world.
                this.playerId = environmentManager.spawn(EntityKind.PLAYER);
                this.playerConnectionMapper.set(playerId, new PlayerConnection(ctx));

                eventBus.post(new PlayerJoinedEvent(this.playerId));

                // TODO: this should move to other systems (event subscribe)
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

        eventBus.post(new PacketReceivedEvent(this.playerId, packet));
    }

    public void disconnect(String reason) {
        ctx.writeAndFlush(new DisconnectReason(reason));
        ctx.close();
    }
}
