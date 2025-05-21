package ru.suvorov.client;

import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class ClientConnection {
    private final String host;
    private final int port;
    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final int RECONNECT_DELAY_MS = 1000;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object sendRequest(CommandRequest request) {
        SocketChannel channel = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        int attempts = 0;
        while (attempts < MAX_RECONNECT_ATTEMPTS) {
            try {
                channel = SocketChannel.open();
                channel.connect(new InetSocketAddress(host, port));
                channel.configureBlocking(true);
                oos = new ObjectOutputStream(channel.socket().getOutputStream());
                ois = new ObjectInputStream(channel.socket().getInputStream());

                // Отправляем объект команды
                oos.writeObject(request);
                oos.flush();

                // Получаем ответ (ExecutionResponse или коллекцию)
                Object response = ois.readObject();
                return response;
            } catch (IOException | ClassNotFoundException e) {
                attempts++;
                if (attempts < MAX_RECONNECT_ATTEMPTS) {
                    System.err.println("Не удалось подключиться к серверу. Попытка " + attempts + " из " + MAX_RECONNECT_ATTEMPTS);
                    try {
                        TimeUnit.MILLISECONDS.sleep(RECONNECT_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    return new ExecutionResponse(false, "Ошибка: " + e.getMessage());
                }
            } finally {
                try {
                    if (oos != null) oos.close();
                    if (ois != null) ois.close();
                    if (channel != null) channel.close();
                } catch (IOException e) {
                    System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
                }
            }
        }
        return new ExecutionResponse(false, "Ошибка: не удалось подключиться к серверу");
    }

    public void close() {
        // Метод больше не нужен, но оставлен для совместимости
    }
} 