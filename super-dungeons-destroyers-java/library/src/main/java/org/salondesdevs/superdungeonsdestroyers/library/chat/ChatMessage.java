package org.salondesdevs.superdungeonsdestroyers.library.chat;

import org.salondesdevs.superdungeonsdestroyers.library.utils.NettySerializable;

import io.netty.buffer.ByteBuf;

public class ChatMessage implements NettySerializable {
    private String content;
    private ChatChannel channel;

    public ChatMessage() {
        this("", null);
    }

    private ChatMessage(String content, ChatChannel chatChannel) {
        this.content = content;
        this.channel = chatChannel;
    }

    @Override
    public void read(ByteBuf in) {
        this.content = readString(in);
        this.channel = readEnum(ChatChannel.class, in);
    }

    @Override
    public void write(ByteBuf out) {
        writeString(this.content, out);
        writeEnum(this.channel, out);
    }

    public String getContent() {
        return content;
    }

    public String toDisplayString() {
        return "[" + this.channel.name() + "] " + this.content;
    }

    public boolean isCommand() {
        return this.content.startsWith("/");
    }

    public static ChatMessage text(String text, ChatChannel channel) {
        return new ChatMessage(text, channel);
    }

    public ChatMessage prepend(ChatMessage text) {
        return new ChatMessage(text.content + this.content, this.channel);
    }
}
