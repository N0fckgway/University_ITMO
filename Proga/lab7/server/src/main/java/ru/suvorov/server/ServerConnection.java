package ru.suvorov.server;


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



    private class ClientHandler implements Runnable {
        private final Socket clientSocket;


        public ClientHandler(Socket socket) {
            this.clientSocket = socket;

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