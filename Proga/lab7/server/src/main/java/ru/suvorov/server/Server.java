package ru.suvorov.server;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.suvorov.server.db.DBConnector;
import ru.suvorov.server.db.DBManager;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Runner;
import ru.suvorov.server.util.StadartConsole;
import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import ru.suvorov.server.commands.interfaces.Executable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public final class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final int DEFAULT_PORT = 1919;
    private static CollectionManager collectionManager;
    @Getter
    private static final Map<String, Executable> commandRegistry = new HashMap<>();
    private static final ExecutorService processingPool = Executors.newCachedThreadPool();
    private static final ForkJoinPool sendingPool = new ForkJoinPool();
    private static Runner runner;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Завершение работы сервера...");
        }));
    }

    public static void main(String[] args) {
        StadartConsole console = new StadartConsole();
        DBConnector dbConnector = new DBConnector();
        DBManager dbManager = new DBManager(dbConnector);


        try {
            logger.info("Запуск сервера...");
            collectionManager = new CollectionManager();
            if (!collectionManager.init()) {
                logger.error("Ошибка при инициализации базы данных");
                System.exit(1);
            } else collectionManager.init();


            runner = new Runner(console, collectionManager, dbConnector, dbManager);
            commandRegistry.putAll(runner.getCommands());
            

            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            logger.info("Сервер запущен на порту {}", DEFAULT_PORT);
            console.println("Сервер запускается...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> readRequest(clientSocket)).start();
                } catch (IOException e) {
                    if (serverSocket.isClosed()) {
                        logger.info("Серверный сокет закрыт, завершение работы.");
                        break;
                    }
                    logger.error("Ошибка при принятии клиентского подключения", e);
                }
            }
        } catch (IOException e) {
            logger.error("Не удалось запустить сервер на порту {}", DEFAULT_PORT, e);
            System.exit(1);
        }
    }

    private static void readRequest(Socket clientSocket) {
        String clientAddress = clientSocket.getRemoteSocketAddress().toString();
        logger.info("Получено новое подключение от {}", clientAddress);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

            // 2. Обработка в CachedThreadPool
            processingPool.submit(() -> processRequest(ois, oos, clientSocket, clientAddress));

        } catch (IOException e) {
            logger.error("Ошибка при создании потоков для клиента {}: {}", clientAddress, e.getMessage());
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    private static void processRequest(ObjectInputStream ois, ObjectOutputStream oos, Socket socket, String clientAddress) {
        try {
            logger.info("Начинаем обработку запроса от {}", clientAddress);
            Object requestObject = ois.readObject();
            logger.info("Прочитан объект запроса: {}", requestObject.getClass().getSimpleName());
            
            if (requestObject instanceof CommandRequest request) {
                logger.info("Получен запрос от {}: команда = {}, аргумент = {}", clientAddress, request.getCommandName(), request.getArgument());
                logger.info("Начинаем выполнение команды: {}", request.getCommandName());
                Object response = processCommand(request);
                logger.info("Команда выполнена, отправляем ответ: {}", response);
                sendingPool.submit(() -> sendResponse(response, oos, socket, clientAddress));
            } else {
                logger.warn("Получен неизвестный тип запроса: {}", requestObject.getClass().getSimpleName());
                ExecutionResponse response = new ExecutionResponse(false, "Ошибка: неизвестный тип запроса");
                sendingPool.submit(() -> sendResponse(response, oos, socket, clientAddress));
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Ошибка при чтении/обработке запроса от {}: {}", clientAddress, e.getMessage(), e);
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private static void sendResponse(Object response, ObjectOutputStream oos, Socket socket, String clientAddress) {
        try (socket; oos) {
            oos.writeObject(response);
            oos.flush();
            logger.debug("Отправлен ответ клиенту {}: {}", clientAddress, response);
        } catch (IOException e) {
            logger.error("Ошибка при отправке ответа клиенту {}: {}", clientAddress, e.getMessage());
        }
    }

    private static Object processCommand(CommandRequest request) {
        String cmd = request.getCommandName().trim().toLowerCase();
        logger.info("Обрабатываем команду: '{}'", cmd);
        
        Executable command = commandRegistry.get(cmd);
        if (command == null) {
            logger.warn("Неизвестная команда: {}", cmd);
            return new ExecutionResponse(false, "Неизвестная команда: " + cmd);
        }
        
        logger.info("Найдена команда в реестре: {}", command.getClass().getSimpleName());
        try {
            if (cmd.equals("show")) {
                logger.info("Выполняем команду show");
                return collectionManager.show();
            }
            
            logger.info("Вызываем метод apply для команды: {}", cmd);
            Object result = command.apply(request.getArgument(), request);
            logger.info("Команда {} выполнена успешно, результат: {}", cmd, result);
            return result;
        } catch (Exception e) {
            logger.error("Ошибка при выполнении команды {}: {}", cmd, e.getMessage(), e);
            return new ExecutionResponse(false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}