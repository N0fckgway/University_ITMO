package ru.suvorov.utilities.collection.commands;

import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.utilities.collection.commands.interfaces.Executable;
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
    public ExecutionResponse apply(String arg) {
        // Получаем информацию о коллекции
        StringBuilder info = new StringBuilder();
        info.append("Информация о коллекции:\n")
                .append("Тип коллекции: ").append(collectionManager.getCollection().getClass().getSimpleName()).append("\n")
                .append("Дата инициализации: ").append(collectionManager.getLastInitTime() != null ? collectionManager.getLastInitTime().toString() : "Не инициализировано").append("\n")
                .append("Дата последнего сохранения: ").append(collectionManager.getLastSaveTime() != null ? collectionManager.getLastSaveTime().toString() : "Не сохранено").append("\n")
                .append("Количество элементов: ").append(collectionManager.getCollection().size()).append("\n");

        // Выводим информацию в консоль
        console.print(info.toString());

        // Возвращаем успешный ответ
        return new ExecutionResponse(true, "Информация о коллекции выведена.");
    }
}
