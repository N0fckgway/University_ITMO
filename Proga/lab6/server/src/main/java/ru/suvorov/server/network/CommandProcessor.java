package ru.suvorov.server.network;

import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.CommandManager;
import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;
import ru.suvorov.model.City;
import java.util.List;
import java.util.stream.Collectors;

public class CommandProcessor {
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;

    public CommandProcessor(CommandManager commandManager, CollectionManager collectionManager) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    public Object processCommand(CommandRequest request) {
        if (request == null || request.getCommandName() == null) {
            return new ExecutionResponse(false, "Ошибка: пустой запрос");
        }
        try {
            String cmd = request.getCommandName().trim();
            if (cmd.equalsIgnoreCase("save")) {
                return new ExecutionResponse(false, "Ошибка: команда save доступна только на сервере");
            }
            if (cmd.equalsIgnoreCase("show")) {
                // Сортировка коллекции через Stream API
                List<City> sorted = collectionManager.getCollection().stream()
                        .sorted()
                        .collect(Collectors.toList());
                return sorted;
            }
            // Пример для других команд
            return new ExecutionResponse(true, "Команда выполнена: " + cmd);
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }

    public void saveCollection() {
        collectionManager.save();
    }
} 