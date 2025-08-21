package ru.suvorov.server.commands;

import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;
import ru.suvorov.model.City;

import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.managers.ZonedDateTimeAdapter;
import ru.suvorov.server.util.Console;
import com.google.gson.*;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


public class Show extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        ConcurrentLinkedQueue<City> collection = collectionManager.show();
        collection.stream()
                .sorted()
                .map(City::toString)
                .collect(Collectors.joining("\n"));

        return new ExecutionResponse(true, "\n Все элементы выведены!");
    }

}
