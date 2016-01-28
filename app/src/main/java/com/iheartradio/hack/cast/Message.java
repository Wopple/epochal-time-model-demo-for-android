package com.iheartradio.hack.cast;

public class Message {
    public final MessageType type;
    public final Object payload;

    public Message(final MessageType type) {
        this(type, null);
    }

    public Message(final MessageType type, final Object payload) {
        this.type = type;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "" + type + " " + payload;
    }
}
