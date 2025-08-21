package ru.suvorov.server.commands;

import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.Console;

public class Info extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        StringBuilder info = new StringBuilder();
        info.append("Информация о коллекции:\n")
                .append("Тип коллекции: ").append(collectionManager.info().getClass().getSimpleName()).append("\n")
                .append("Дата инициализации: ").append(collectionManager.info() != null ? collectionManager.getLastInitTime().toString() : "Не инициализировано")
                .append("Количество элементов: ").append(collectionManager.info().size()).append("\n");

        return new ExecutionResponse(true, info.toString());
    }
}
