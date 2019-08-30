package org.salondesdevs.superdungeonsdestroyers.library.chat;

import io.netty.buffer.ByteBuf;
import org.salondesdevs.superdungeonsdestroyers.library.utils.NettySerializable;

public class ChatMessage implements NettySerializable {
    public String content;

    public ChatMessage() {
        this("");
    }

    private ChatMessage(String content) {
        this.content = content;
    }

    @Override
    public void read(ByteBuf in) {
        this.content = readString(in);
    }

    @Override
    public void write(ByteBuf out) {
        writeString(this.content, out);
    }

    public String toDisplayString() {
        return this.content;
    }

    public ChatMessage prepend(ChatMessage chatMessage) {
        return new ChatMessage(chatMessage.content + this.content);
    }

    public boolean isCommand() {
        return this.content.startsWith("/");
    }

    public static ChatMessage text(String text) {
        return new ChatMessage(text);
    }
}
