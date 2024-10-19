package com.example.mentalhealthtracker.ui.chat;

public class ChatMessage {
    public static final int TYPE_USER = 0;
    public static final int TYPE_AI = 1;

    private final String message;
    private final int messageType;
    private final long timestamp;

    public ChatMessage(String message, int messageType) {
        this.message = message;
        this.messageType = messageType;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() { return message; }
    public int getMessageType() { return messageType; }
    public long getTimestamp() { return timestamp; }
}