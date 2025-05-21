package ru.suvorov.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.CommandManager;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnection {
    private final int port;
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private final ExecutorService threadPool;
    private ServerSocket serverSocket;
    private boolean running;

    public ServerConnection(int port, CollectionManager collectionManager, CommandManager commandManager) {
        this.port = port;
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Сервер запущен на порту " + port);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Ошибка при принятии соединения: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при остановке сервера: " + e.getMessage());
        }
        threadPool.shutdown();
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final Gson gson;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.gson = new GsonBuilder().create();
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                while (running) {
                    try {
                        String request = (String) in.readObject();
                        String response = commandManager.executeCommand(request, Command.getCommand());
                        out.writeObject(response);
                        out.flush();
                    } catch (ClassNotFoundException e) {
                        System.err.println("Ошибка при чтении запроса: " + e.getMessage());
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при обработке клиента: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
                }
            }
        }
    }
} 