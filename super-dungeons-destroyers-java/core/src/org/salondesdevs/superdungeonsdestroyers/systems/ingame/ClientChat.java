package org.salondesdevs.superdungeonsdestroyers.systems.ingame;

import com.badlogic.gdx.Input;
import com.google.common.eventbus.Subscribe;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.events.input.KeyPressedEvent;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatChannel;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatMessage;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens.ChatScreen;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ClientChat extends Service {

    public List<ChatMessage> messages;

    @Inject
    UiSystem uiSystem;

    @Override
    public void initialize() {
        this.messages = new ArrayList<>();
    }

    public void onMessage(ChatChannel chatChannel, ChatMessage chatMessage) {
        this.messages.add(chatMessage);
    }

    @Subscribe
    public void onKeyTyped(KeyPressedEvent keyPressedEvent) {
        if (keyPressedEvent.getKeycode() == Input.Keys.T) {
            uiSystem.displayScreen(ChatScreen.class);
        }
    }
}
