package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.Input;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import com.google.inject.Injector;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.events.ChatEvent;
import org.salondesdevs.superdungeonsdestroyers.events.KeyReleasedEvent;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatMessage;
import org.salondesdevs.superdungeonsdestroyers.library.events.core.EventBus;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver.FromServerChat;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.PacketReceivedEvent;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.ChatScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ClientChat extends Service {

    private static final Logger logger = LoggerFactory.getLogger(ClientChat.class);

    public List<ChatMessage> messages;

    @Inject
    UiSystem uiSystem;

    @Inject
    EventBus eventBus;

    @Inject
    Injector injector;

    ChatScreen chatScreen;

    @Override
    public void initialize() {
        this.messages = new ArrayList<>();
        chatScreen = injector.getInstance(ChatScreen.class);
    }

    @EventHandler
    public void onPacketReceived(PacketReceivedEvent packetReceivedEvent) {
        if (packetReceivedEvent.getPacket() instanceof FromServerChat) {
            FromServerChat fromServerChat = ((FromServerChat) packetReceivedEvent.getPacket());
            ChatMessage chatMessage = fromServerChat.getChatMessage();
            this.eventBus.post(new ChatEvent(chatMessage));
            this.messages.add(chatMessage);
        }
    }

    @EventHandler
    public void onKeyTyped(KeyReleasedEvent keyPressedEvent) {
        if (keyPressedEvent.getKeycode() == Input.Keys.T) {
            uiSystem.displayScreen(ChatScreen.class);
        }
    }
}
