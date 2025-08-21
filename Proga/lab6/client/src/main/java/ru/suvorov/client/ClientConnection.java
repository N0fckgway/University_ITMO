package ru.suvorov.client;

import ru.suvorov.CommandRequest;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class ClientConnection {
    private final String host;
    private final int port;
    private static final int RECONNECT_DELAY_MS = 5000;
    private SocketChannel channel;
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
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException e) {

                    }
                }
                channel = SocketChannel.open();
                channel.connect(new InetSocketAddress(host, port));
                channel.configureBlocking(true);
                
                oos = new ObjectOutputStream(channel.socket().getOutputStream());
                ois = new ObjectInputStream(channel.socket().getInputStream());
                isConnected = true;
                System.out.println("Успешно подключено к серверу");
                return;
            } catch (IOException e) {
                System.err.println("Не удалось подключиться к серверу. Повторная попытка через " + (RECONNECT_DELAY_MS/1000) + " секунд...");
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
                if (channel == null || !channel.isConnected() || !isConnected) {
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
                    connect(); // Переподключаемся при ошибке
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
            if (channel != null) channel.close();
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
} 