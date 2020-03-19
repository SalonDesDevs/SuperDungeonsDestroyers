package org.salondesdevs.superdungeonsdestroyers.library.packets.fromserver;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatMessage;
import org.salondesdevs.superdungeonsdestroyers.library.packets.Packet;

public class FromServerChat extends Packet {
    private ChatMessage chatMessage;

    public FromServerChat() {
    }

    public FromServerChat(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public void read(ByteBuf in) {
        this.chatMessage = new ChatMessage();
        this.chatMessage.read(in);
    }

    @Override
    public void write(ByteBuf out) {
        this.chatMessage.write(out);
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}
