package ru.suvorov.server.commands;

import ru.suvorov.ExecutionResponse;
import ru.suvorov.model.City;

import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.managers.ZonedDateTimeAdapter;
import ru.suvorov.server.util.Console;
import com.google.gson.*;

import java.time.ZonedDateTime;


public class Show extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(Object arg) {
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(true, "Коллекция пуста");

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        for (var collection : collectionManager.getCollection()){
            console.println(gson.toJson(collection));
            console.println("");
        }
        return new ExecutionResponse(true, "\n Все элементы выведены!");
    }

}
