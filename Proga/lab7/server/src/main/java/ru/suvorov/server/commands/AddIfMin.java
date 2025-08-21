package ru.suvorov.server.commands;



import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;

import ru.suvorov.model.City;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.db.DBConnector;
import ru.suvorov.server.db.DBManager;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

import ru.suvorov.server.util.Console;
import ru.suvorov.server.util.ReadObject;
import ru.suvorov.util.CollectionElement;

import ru.suvorov.exception.InvalidDataException;

import java.util.Comparator;
import java.util.LinkedList;



public class AddIfMin extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;



    public AddIfMin(Console console, CollectionManager collectionManager) {
        super("add_if_min {element} :", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }


    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) throws Exception {
        try {
            if (!(arg instanceof City newCity)) {
                return new ExecutionResponse(false, "Ошибка: ожидался объект City.");
        }
            newCity.validate();
        LinkedList<City> collection = collectionManager.getCollection();
        City minCity = collection.stream().min(Comparator.comparingLong(City::getId)).orElse(null);
        if (minCity == null || newCity.getId() < minCity.getId()) {
            collectionManager.add(newCity);
            return new ExecutionResponse(true, "Элемент добавлен, так как его значение меньше, чем у наименьшего элемента");
        } else return new ExecutionResponse(false, "Новый элемент не меньше наименьшего элемента в коллекции");
        } catch (InvalidDataException e) {
            return new ExecutionResponse(false, e.getMessage());
        }
    }

    private City parseCity(String arg) {
        try {
            Long id = Long.parseLong(arg.trim());
            return ReadObject.readCity(console);
        } catch (Exception e) {
            console.println("ошибка при парсинге данных для нового города");
            return null;

        }
    }
}
