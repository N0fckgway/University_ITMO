package ru.suvorov.server.commands;



import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.CollectionElement;
import ru.suvorov.server.util.Console;
import ru.suvorov.server.collection.ReadObject;
import ru.suvorov.server.collection.model.City;

import java.util.LinkedList;


public class AddIfMin extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;
    private final CollectionElement collectionElement;


    public AddIfMin(Console console, CollectionManager collectionManager, CollectionElement collectionElement) {
        super("add_if_min {element} :", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
        this.collectionElement = collectionElement;
    }


    @Override
    public ExecutionResponse apply(String arg) throws Exception {
        if (arg == null || arg.isEmpty()) {
            return new ExecutionResponse(false, "Не указан элемент для добавления");
        }

        City newCity = parseCity(arg);
        if (newCity == null) {
            return new ExecutionResponse(false, "Не удалось создать элемент из строки");
        }

        LinkedList<City> collection = collectionManager.getCollection();
        City minCity = collection.stream().min((c1, c2) -> Long.compare(c1.getId(), c2.getId())).orElse(null);
        if (minCity == null || newCity.getId() < minCity.getId()) {
            collectionManager.add(newCity);
            return new ExecutionResponse(true, "Элемент добавлен, так как его значение меньше, чем у наименьшего элемента");
        } else return new ExecutionResponse(false, "Новый элемент не меньше наименьшего элемента в коллекции");
    }

    private City parseCity(String arg) {
        try {
            Long id = Long.parseLong(arg.trim());
            return ReadObject.readCity(console, collectionElement);
        } catch (Exception e) {
            console.println("ошибка при парсинге данных для нового города");
            return null;

        }
    }
}
