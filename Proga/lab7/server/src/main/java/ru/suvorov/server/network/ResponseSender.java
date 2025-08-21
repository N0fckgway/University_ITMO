package ru.suvorov.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseSender {
    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);
    private static final int BUFFER_SIZE = 4096;

    public void sendResponse(SocketChannel channel, String response) throws IOException {
        if (response == null) {
            logger.warn("Попытка отправить null-ответ клиенту {}", channel.getRemoteAddress());
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
        logger.debug("Отправлен ответ клиенту {}: {}", channel.getRemoteAddress(), response);
    }

    public void sendError(SocketChannel channel, String errorMessage) throws IOException {
        String errorResponse = "Ошибка: " + errorMessage;
        logger.error("Отправка ошибки клиенту {}: {}", channel.getRemoteAddress(), errorMessage);
        sendResponse(channel, errorResponse);
    }
} 