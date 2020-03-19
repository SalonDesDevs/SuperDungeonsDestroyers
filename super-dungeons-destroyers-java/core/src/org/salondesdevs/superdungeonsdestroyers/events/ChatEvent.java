package org.salondesdevs.superdungeonsdestroyers.events;

import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatMessage;
import org.salondesdevs.superdungeonsdestroyers.library.events.Event;

public class ChatEvent extends Event {
    private final ChatMessage message;

    public ChatEvent(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
