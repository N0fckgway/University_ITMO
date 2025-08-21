package ru.suvorov.server.commands;


import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;


import ru.suvorov.model.City;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.db.DBConnector;
import ru.suvorov.server.db.DBManager;


import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.Console;


import java.util.Comparator;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class PrintAscending extends Command implements Executable {
    private final Console console;

    public PrintAscending(Console console) {
        super("print_ascending :", "вывести элементы коллекции в порядке возрастания");
        this.console = console;

    }


    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        DBConnector dbConnector = new DBConnector();
        DBManager dbManager = new DBManager(dbConnector);
        if (dbManager.loadCollection() == null || dbManager.loadCollection().isEmpty()) return new ExecutionResponse(false, "Коллекция пуста, или null");

        ConcurrentLinkedQueue<City> collection = dbManager.loadCollection();

        collection.stream()
                .sorted(Comparator.comparingLong(City::getId))
                .map(City::toString)
                .collect(Collectors.joining("\n"));
        for (var e : collection) {
            console.println(e);
        }

        return new ExecutionResponse(true, "Элементы коллекции успешно выведены");
    }
}
