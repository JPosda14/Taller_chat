package com.example.taller_chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatClient extends Application {

    public ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ChatController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Chat.fxml"));
        BorderPane root = fxmlLoader.load();
        controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void connect(String serverAddress, int serverPort) throws IOException {
        Socket socket = new Socket(serverAddress, serverPort);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        Thread messageThread = new Thread(new MessageHandler());
        messageThread.start();
    }

    private class MessageHandler implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Chat_messages message = (Chat_messages) inputStream.readObject();
                    controller.addMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}


