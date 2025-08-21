package ru.suvorov.client;

import ru.suvorov.CommandRequest;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientConnection {
    private final String host;
    private final int port;
    private static final int RECONNECT_DELAY_MS = 5000;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean isConnected = false;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
        connect();
    }

    private void connect() {
        while (!isConnected) {
            try {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        
                    }
                }
                socket = new Socket(host, port);
                
                // Сначала создаем и отправляем заголовок oos
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                // Затем создаем ois, который прочитает заголовок от сервера
                ois = new ObjectInputStream(socket.getInputStream());

                isConnected = true;
                System.out.println("Успешно подключено к серверу");
                return;
            } catch (IOException e) {
                System.err.println("Не удалось подключиться к серверу. Повторная попытка через " + (RECONNECT_DELAY_MS / 1000) + " секунд...");
                try {
                    TimeUnit.MILLISECONDS.sleep(RECONNECT_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public Object sendRequest(CommandRequest request) {
        while (true) {
            try {
                if (socket == null || socket.isClosed() || !isConnected) {
                    isConnected = false;
                    connect();
                }
                
                oos.writeObject(request);
                oos.flush();

                return ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Ошибка при отправке запроса. Попытка переподключения...");
                isConnected = false;
                try {
                    TimeUnit.MILLISECONDS.sleep(RECONNECT_DELAY_MS);
                    connect();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Операция прервана", ie);
                }
            }
        }
    }

    public void close() {
        try {
            if (oos != null) oos.close();
            if (ois != null) ois.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
} 