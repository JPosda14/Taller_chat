package com.example.taller_chat;

import java.io.IOException;
import java.io.ObjectOutputStream;

import com.example.taller_chat.Chat_messages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML
    private ListView<String> messageList;

    @FXML
    private TextField messageInput;

    private ObjectOutputStream outputStream;

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @FXML
    private void sendMessage() {
        String message = messageInput.getText();
        messageInput.clear();

        try {
            Chat_messages chatMessage = new Chat_messages("Me", message);
            outputStream.writeObject(chatMessage);
            outputStream.flush();
            addMessage(chatMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(Chat_messages message) {
        String formattedMessage = String.format("[%s] %s: %s", System.currentTimeMillis(), message.getSender(),
                message.getMessage());
        Platform.runLater(() -> messageList.getItems().add(formattedMessage));
    }

}