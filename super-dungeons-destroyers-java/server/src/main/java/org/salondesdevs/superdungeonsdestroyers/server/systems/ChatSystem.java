package org.salondesdevs.superdungeonsdestroyers.server.systems;

import com.google.common.eventbus.Subscribe;
import net.wytrem.ecs.Mapper;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatChannel;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatMessage;
import org.salondesdevs.superdungeonsdestroyers.library.components.Name;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.FromClientChat;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.FromServerChat;
import org.salondesdevs.superdungeonsdestroyers.server.events.PacketReceivedEvent;
import org.salondesdevs.superdungeonsdestroyers.server.systems.net.NetworkSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatSystem extends Service {

    private static final Logger logger = LoggerFactory.getLogger(ChatSystem.class);

    @Inject
    NetworkSystem networkSystem;

    @Inject
    Mapper<Name> nameMapper;

    public void broadcast(ChatMessage chatMessage) {
        networkSystem.broadcast(new FromServerChat(chatMessage));
    }

    public void send(int player, String message) {
        this.send(player, ChatMessage.text(message, ChatChannel.SYSTEM));
    }

    public void send(int player, ChatMessage message) {
        networkSystem.send(player, new FromServerChat(message));
    }

    @Subscribe
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof FromClientChat) {
            FromClientChat fromClientChat = ((FromClientChat) packetReceivedEvent.getPacket());
            int player = packetReceivedEvent.getPlayer();
            logger.info("Player {} chatted.", player);

            if (fromClientChat.getChatMessage().isCommand()) {
                // If it is a command, handle it.

                //TODO: better command system
                String line = fromClientChat.getChatMessage().getContent().substring(1); // without the '/'
                String[] args = line.split(" ");

                if (args.length > 0) {
                    if (args[0].equals("myid")) {
                        this.send(player, "Your entity id is " + player);
                    }
                    else if (args[0].equals("name")) {
                        if (args.length < 2) {
                            this.send(player, "/name [your_name]");
                        } else {
                            nameMapper.set(player, new Name(args[1]));
                        }
                    }
                }
            }
            // Otherwise it is a basic message, so broadcast it.
            else {
                if (fromClientChat.getChatChannel() == ChatChannel.SYSTEM) {
                    this.send(player, "You can't speak on [SYSTEM], use /say.");
                } else {
                    String prefix = nameMapper.has(player) ? nameMapper.get(player).getValue() + " : " : "entity#" + player;
                    this.broadcast(fromClientChat.getChatMessage().prepend(ChatMessage.text(prefix, ChatChannel.SYSTEM)));
                    logger.info("broadcasting");
                }
            }
        }
    }
}
