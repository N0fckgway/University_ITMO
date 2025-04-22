package ru.suvorov.server.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.Console;
import ru.suvorov.server.collection.model.City;
import ru.suvorov.server.collection.model.Human;

import java.time.ZonedDateTime;
import java.util.LinkedList;

public class FilterLessThanGovernor extends Command implements Executable {
    private final CollectionManager collectionManager;
    private final Console console;

    public FilterLessThanGovernor(Console console, CollectionManager collectionManager) {
        super("filter_less_than_governor governor :", "вывести элементы, значение поля governor которых меньше заданного");
        this.collectionManager = collectionManager;
        this.console = console;

    }

    @Override
    public ExecutionResponse apply(String arg) {
        if (collectionManager.getCollection() == null || collectionManager.getCollection().isEmpty())
            return new ExecutionResponse(false, "Коллекция пуста или удаление невозможно");


        if (arg == null || arg.isEmpty()) {
            return new ExecutionResponse(false, "Не указан элемент для удаления");

        }

        LinkedList<City> collection = collectionManager.getCollection();
        LinkedList<City> filteredCity = new LinkedList<>();
        Human governor = parseGovernor(arg.trim());
        if (governor == null) {
            return new ExecutionResponse(false, "Ошибка при создании governor из строки");
        }

        for (City city : collection) {
            if (city.getGovernor().compareTo(governor) < 0) {
                filteredCity.add(city);
            }
        }
        return new ExecutionResponse(true, "Элементы дропнуты");
    }

    private Human parseGovernor(String arg) {
        try {
            String[] parts = arg.trim().split(" ");
            if (parts.length < 4) {
                System.out.println("Нужно ввести: имя возраст рост день_рождения");
                return null;
            }

            String name = parts[0];
            int age = Integer.parseInt(parts[1]);
            float height = Float.parseFloat(parts[2]);
            ZonedDateTime birthday = ZonedDateTime.parse(parts[3]);

            if (name.isEmpty() || age <= 0 || height <= 0) {
                console.printError("Некорректные значения: имя пустое, возраст <= 0 или рост <= 0");
                return null;
            }

            return new Human(name, age, height, birthday);
        } catch (Exception e) {
            console.printError("Ошибка при разборе governor: " + e.getMessage());
            return null;
        }
    }

}
