package ru.suvorov.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.util.CommandManager;
import ru.suvorov.server.managers.DumpManager;
import ru.suvorov.server.util.StadartConsole;
import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;
import ru.suvorov.model.City;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.commands.*;
import ru.suvorov.server.util.Console;
import ru.suvorov.util.CollectionElement;
import java.util.Map;
import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.builder.ConcreateCoordinatesBuilder;
import ru.suvorov.server.builder.ConcreateHumanBuilder;

public final class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final int DEFAULT_PORT = 1919;
    private static CollectionManager collectionManager;
    private static final Map<String, Executable> commandRegistry = new java.util.HashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Завершение работы сервера...");
            if (collectionManager != null && collectionManager.save()) {
                logger.info("Коллекция успешно сохранена");
            } else {
                logger.error("Ошибка при сохранении коллекции");
            }
        }));
    }

    public static void main(String[] args) {
        StadartConsole console = new StadartConsole();

        if (args.length == 0) {
            logger.error("Не указано имя файла коллекции");
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        try {
            logger.info("Запуск сервера...");
            DumpManager dumpManager = new DumpManager(args[0], console);
            collectionManager = new CollectionManager(dumpManager);
            if (!collectionManager.init()) {
                logger.error("Ошибка при инициализации коллекции");
                console.printError("Ошибка при инициализации коллекции");
                System.exit(1);
            }
            logger.info("Коллекция успешно инициализирована");
            CommandManager commandManager = new CommandManager();

            // --- Регистрируем команды только теперь! ---
            CollectionElement collectionElement = null;
            ConcreteCityBuilder concreteCityBuilder = new ConcreteCityBuilder();
            ConcreateCoordinatesBuilder concreateCoordinatesBuilder = new ConcreateCoordinatesBuilder();
            ConcreateHumanBuilder concreateHumanBuilder = new ConcreateHumanBuilder();

            commandRegistry.put("help", new Executable() {
                @Override
                public ExecutionResponse apply(Object arg) {
                    StringBuilder helpMessage = new StringBuilder();
                    helpMessage.append("Доступные команды:\n");
                    helpMessage.append("help - вывести справку по доступным командам\n");
                    helpMessage.append("show - вывести все элементы коллекции\n");
                    helpMessage.append("add - добавить новый элемент в коллекцию\n");
                    helpMessage.append("add_if_min - добавить новый элемент, если он меньше минимального\n");
                    helpMessage.append("update_id - обновить элемент по id\n");
                    helpMessage.append("remove_by_id - удалить элемент по id\n");
                    helpMessage.append("remove_greater - удалить элементы, превышающие заданный\n");
                    helpMessage.append("clear - очистить коллекцию\n");
                    helpMessage.append("info - вывести информацию о коллекции\n");
                    helpMessage.append("print_ascending - вывести элементы в порядке возрастания\n");
                    helpMessage.append("sum_of_meters_above_sea_level - сумма значений metersAboveSeaLevel\n");
                    helpMessage.append("filter_less_than_governor - вывести элементы с governor меньше заданного\n");
                    helpMessage.append("generate_random_obj - добавить случайные элементы\n");
                    helpMessage.append("exit - завершить работу клиента\n");
                    return new ExecutionResponse(true, helpMessage.toString());
                }
            });
            commandRegistry.put("show", new Show(console, collectionManager));
            commandRegistry.put("add", new Add(console, collectionManager, collectionElement));
            commandRegistry.put("add_if_min", new AddIfMin(console, collectionManager, collectionElement));
            commandRegistry.put("clear", new Clear(collectionManager));
            commandRegistry.put("exit", new Exit());
            commandRegistry.put("execute_script", new ExecuteScript(console));
            commandRegistry.put("filter_less_than_governor", new FilterLessThanGovernor(console, collectionManager));
            commandRegistry.put("generate_random_obj", new GenerateRandomObj(collectionManager, concreateCoordinatesBuilder, concreteCityBuilder, concreateHumanBuilder));
            commandRegistry.put("info", new Info(console, collectionManager));
            commandRegistry.put("print_ascending", new PrintAscending(console, collectionManager));
            commandRegistry.put("remove_by_id", new RemoveById(console, collectionManager));
            commandRegistry.put("remove_first", new RemoveFirst(collectionManager));
            commandRegistry.put("remove_greater", new RemoveGreater(console, collectionManager, collectionElement));
            commandRegistry.put("save", new Save(collectionManager));
            commandRegistry.put("sum_of_meters_above_sea_level", new SumOfMetersAboveSeaLevel(collectionManager));
            commandRegistry.put("update_id", new UpdateId(console, collectionManager, collectionElement));
            // --- конец регистрации команд ---

            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            logger.info("Сервер запущен на порту {}", DEFAULT_PORT);
            console.println("Сервер запускается...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            logger.error("Ошибка при запуске сервера: {}", e.getMessage());
            console.printError("Ошибка при запуске сервера: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void handleClient(Socket clientSocket) {
        String clientAddress = null;
        try {
            clientAddress = clientSocket.getRemoteSocketAddress().toString();
            logger.info("Получено новое подключение от {}", clientAddress);
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            Object obj = ois.readObject();
            System.out.println("DEBUG: Получен объект: " + obj);
            if (obj instanceof CommandRequest) {
                CommandRequest request = (CommandRequest) obj;
                logger.debug("Получен запрос от {}: {}", clientAddress, request);
                Object response = processCommand(request);
                System.out.println("DEBUG: Отправляю ответ: " + response);
                oos.writeObject(response);
                oos.flush();
                logger.debug("Отправлен ответ клиенту {}: {}", clientAddress, response);
            } else {
                ExecutionResponse response = new ExecutionResponse(false, "Ошибка: неизвестный тип запроса");
                oos.writeObject(response);
                oos.flush();
            }
        } catch (Exception e) {
            logger.error("Ошибка при обработке клиента " + clientAddress, e);
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                logger.info("Соединение с клиентом {} закрыто", clientAddress);
            } catch (IOException e) {
                logger.error("Ошибка при закрытии соединения с клиентом {}: {}", clientAddress, e.getMessage());
            }
        }
    }

    private static Object processCommand(CommandRequest request) {
        String cmd = request.getCommandName().trim().toLowerCase();
        Executable command = commandRegistry.get(cmd);
        if (command == null) {
            return new ExecutionResponse(false, "Неизвестная команда: " + cmd);
        }
        try {
            // Для show возвращаем коллекцию
            if (cmd.equals("show")) {
                List<City> sorted = collectionManager.getCollection().stream().sorted().toList();
                return sorted;
            }
            // Для остальных команд вызываем apply с оригинальным аргументом
            return command.apply(request.getArgument());
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }

    public static Map<String, Executable> getCommandRegistry() {
        return commandRegistry;
    }
} 