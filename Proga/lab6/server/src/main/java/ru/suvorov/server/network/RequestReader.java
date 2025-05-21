package ru.suvorov.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);
    private static final int BUFFER_SIZE = 4096;
    private final ByteBuffer buffer;

    public RequestReader() {
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        logger.debug("RequestReader инициализирован с размером буфера {}", BUFFER_SIZE);
    }

    public String readRequest(SocketChannel channel) throws IOException {
        buffer.clear();
        StringBuilder requestBuilder = new StringBuilder();
        int bytesRead;

        while ((bytesRead = channel.read(buffer)) > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            requestBuilder.append(new String(bytes));
            buffer.clear();
        }

        if (bytesRead == -1) {
            logger.debug("Клиент закрыл соединение: {}", channel.getRemoteAddress());
            channel.close();
            return null;
        }

        String request = requestBuilder.toString().trim();
        logger.debug("Прочитан запрос от {}: {}", channel.getRemoteAddress(), request);
        return request;
    }
} 