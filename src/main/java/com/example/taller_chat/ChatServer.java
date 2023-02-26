package com.example.taller_chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private int port;
    private List<ObjectOutputStream> clients = new ArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Chat server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            clients.add(outputStream);

            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                inputStream = new ObjectInputStream(clientSocket.getInputStream());

                while (true) {
                    Chat_messages message = (Chat_messages) inputStream.readObject();
                    System.out.println("Received message: " + message.getMessage());

                    for (ObjectOutputStream client : clients) {
                        if (client != outputStream) {
                            client.writeObject(message);
                            client.flush();
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    inputStream.close();
                    clientSocket.close();
                    clients.remove(outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer(1234);
        server.start();
    }

}

