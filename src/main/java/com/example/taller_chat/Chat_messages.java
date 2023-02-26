package com.example.taller_chat;

import java.io.Serializable;

public class Chat_messages implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sender;
    private String message;

    public Chat_messages(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

}

