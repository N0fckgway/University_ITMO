package ru.suvorov.server.commands;


import ru.suvorov.ExecutionResponse;

import ru.suvorov.model.City;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

import ru.suvorov.server.util.Console;
import ru.suvorov.server.util.ReadObject;
import ru.suvorov.util.CollectionElement;


import java.util.LinkedList;


public class RemoveGreater extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;
    private final CollectionElement collectionElement;

    public RemoveGreater(Console console, CollectionManager collectionManager, CollectionElement collectionElement) {
        super("remove_greater {element} :", "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
        this.console = console;
        this.collectionElement = collectionElement;

    }


    @Override
    public ExecutionResponse apply(Object arg) {
        if (collectionManager.getCollection() == null || collectionManager.getCollection().isEmpty())
            return new ExecutionResponse(false, "Коллекция пуста или удаление невозможно");


        if (arg == null) {
            return new ExecutionResponse(false, "Не указан элемент для удаления");
        }

        City newCity = parseCity(arg);

        if (newCity == null) {
            return new ExecutionResponse(false, "Не удалось создать элемент для сравнения");

        }
        LinkedList<City> collection = collectionManager.getCollection();
        collection.removeIf(city -> city.getId() > newCity.getId());
        return new ExecutionResponse(true, "Элементы, превышающие заданный, успешно удалены");
    }


    private City parseCity(Object arg) {
            try {
                Long id = Long.parseLong(((String) arg).trim());
            return ReadObject.readCity(console, collectionElement);
        } catch (Exception e) {
            console.println("ошибка при парсинге данных для нового города");
            return null;

        }
    }
}
