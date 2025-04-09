package ru.suvorov.server.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;

import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.Console;
import ru.suvorov.server.collection.model.City;


import java.util.LinkedList;

public class PrintAscending extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintAscending(Console console, CollectionManager collectionManager) {
        super("print_ascending :", "вывести элементы коллекции в порядке возрастания");
        this.console = console;
        this.collectionManager = collectionManager;

    }


    @Override
    public ExecutionResponse apply(String arg) throws Exception {
        if (collectionManager.getCollection() == null || collectionManager.getCollection().isEmpty()) return new ExecutionResponse(false, "Коллекция пуста, или null");

        LinkedList<City> collection = collectionManager.getCollection();
        collection.sort((c1, c2) -> Long.compare(c1.getId(), c2.getId()));
        for (var e : collection) {
            console.println(e);
        }

        return new ExecutionResponse(true, "Элементы коллекции успешно выведены");
    }
}
