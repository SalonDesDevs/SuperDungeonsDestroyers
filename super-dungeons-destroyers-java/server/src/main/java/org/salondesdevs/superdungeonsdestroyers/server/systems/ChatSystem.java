package org.salondesdevs.superdungeonsdestroyers.server.systems;

import org.salondesdevs.superdungeonsdestroyers.library.components.RemainingSteps;
import org.salondesdevs.superdungeonsdestroyers.library.components.Role;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
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
import java.util.Map;

@Singleton
public class ChatSystem extends Service {

    private static final Logger logger = LoggerFactory.getLogger(ChatSystem.class);

    @Inject
    NetworkSystem networkSystem;

    @Inject
    Mapper<Name> nameMapper;

    @Inject
    Mapper<Role> roleMapper;

    @Inject
    Mapper<RemainingSteps> remainingStepsMapper;

    public void broadcast(ChatMessage chatMessage) {
        networkSystem.broadcast(new FromServerChat(chatMessage));
    }

    public void send(int player, String message) {
        this.send(player, ChatMessage.text(message, ChatChannel.SYSTEM));
    }

    public void send(int player, ChatMessage message) {
        networkSystem.send(player, new FromServerChat(message));
    }

    @EventHandler
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof FromClientChat) {
            FromClientChat fromClientChat = ((FromClientChat) packetReceivedEvent.getPacket());
            int player = packetReceivedEvent.getPlayer();
            logger.info("Player {} chatted.", player);

            if (fromClientChat.getChatMessage().isCommand()) {
                // If it is a command, handle it.

                //TODO: better command system
                String line = fromClientChat.getChatMessage().getContent().substring(1); // without the '/'
                String[] tokens = line.split(" ");

                if (tokens.length > 0) {
                    if (tokens[0].equals("help")) {

                    }
                    else if (tokens[0].equals("myid")) {
                        this.send(player, "Your entity id is " + player);
                    }
                    else if (tokens[0].equals("name")) {
                        if (tokens.length < 2) {
                            this.send(player, "/name [your_name]");
                        } else {
                            nameMapper.set(player, new Name(tokens[1]));
                        }
                    }
                    else if (tokens[0].equals("role")) {
                        if (tokens.length < 2) {
                            this.send(player, "/role [your_role]");
                        }
                        else {
                            try {
                                Role role = Role.valueOf(tokens[1].toUpperCase());
                                roleMapper.set(player, role);
                                this.send(player, "Your role is now " + role);
                            }
                            catch (Exception ex) {
                                this.send(player, "Invalid role " + tokens[1]);
                            }
                        }
                    }
                    else if (tokens[0].equals("steps")) {
                        if (tokens.length == 2) {
                            if (tokens[1].equals("unset")) {
                                remainingStepsMapper.unset(player);
                            }
                            else {
                                this.send(player, "/steps set|add|unset <amount>");
                            }
                        }
                        else if (tokens.length < 3) {
                            this.send(player, "/steps set|add|unset <amount>");
                        }
                        else {
                            int count = Integer.parseInt(tokens[2]);

                            if (tokens[1].equals("set")) {
                                remainingStepsMapper.set(player, new RemainingSteps(count));
                            }
                            else if (tokens[1].equals("add")) {
                                if (remainingStepsMapper.has(player)) {
                                    remainingStepsMapper.get(player).add(count);
                                }
                                else {
                                    remainingStepsMapper.set(player, new RemainingSteps(count));
                                }
                            }
                            else {
                                this.send(player, "Unknown command.");
                            }
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
                }
            }
        }
    }
}
