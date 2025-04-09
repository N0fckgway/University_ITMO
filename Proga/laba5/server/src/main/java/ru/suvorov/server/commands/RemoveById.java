package ru.suvorov.server.commands;

import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.Console;

public class RemoveById extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id id: ", "удалить элемент из коллекции по его id");
        this.console = console;
        this.collectionManager = collectionManager;

    }


    @Override
    public ExecutionResponse apply(String arg) {
        if (arg == null || arg.trim().isEmpty()) return new ExecutionResponse(false, "Не найден id");
        Long id;
        try {
            id = Long.parseLong(arg.trim());
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "Неверный формат id");

        }

        boolean removeId = collectionManager.remove(id);

        if (!(removeId)) {
            return new ExecutionResponse(false, "Ошибка удаления");
        } else return new ExecutionResponse(true, "Элемент удален");



    }
}
