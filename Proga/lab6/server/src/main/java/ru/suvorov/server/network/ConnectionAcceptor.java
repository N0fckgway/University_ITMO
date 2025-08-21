package ru.suvorov.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ConnectionAcceptor {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionAcceptor.class);
    private final ServerSocketChannel serverChannel;
    private final Selector selector;
    private final int port;
    private boolean running;

    public ConnectionAcceptor(int port) throws IOException {
        this.port = port;
        this.serverChannel = ServerSocketChannel.open();
        this.selector = Selector.open();
        this.running = true;
        logger.debug("ConnectionAcceptor инициализирован для порта {}", port);
    }

    public void start() throws IOException {
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("Сервер запущен на порту {}", port);
    }

    public SocketChannel acceptConnection() throws IOException {
        if (selector.select() > 0) {
            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    logger.info("Новое подключение: {}", client.getRemoteAddress());
                    return client;
                }
            }
        }
        return null;
    }

    public void stop() {
        running = false;
        try {
            if (selector != null) {
                selector.close();
                logger.debug("Selector закрыт");
            }
            if (serverChannel != null) {
                serverChannel.close();
                logger.debug("ServerSocketChannel закрыт");
            }
        } catch (IOException e) {
            logger.error("Ошибка при остановке сервера: {}", e.getMessage());
        }
    }
} 