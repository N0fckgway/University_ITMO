package ru.suvorov.server.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

import ru.suvorov.server.util.CollectionElement;
import ru.suvorov.server.util.Console;
import ru.suvorov.server.collection.ReadObject;
import ru.suvorov.server.collection.model.City;

public class Add extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;
    private final CollectionElement collectionElement;

    public Add(Console console, CollectionManager collectionManager, CollectionElement collectionElement) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
        this.collectionElement = collectionElement;

    }

    @Override
    public ExecutionResponse apply(String arg) {
        try {
            if (!arg.isEmpty())
                return new ExecutionResponse(false, "Неверное кол-во аргументов!\nИспользование: " + getName());
            console.println("Создание нового City: ");
            City city = ReadObject.readCity(console, collectionElement);
            if (city != null) {
                city.validate();
                collectionManager.add(city);
                return new ExecutionResponse("Город добавлен!");
            } else
                return new ExecutionResponse(false, "Поля города невалидны! Город не в кондициях чтобы создаться! Надо что-то делать");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Отмена создания города!");
        }

    }

}
