package ru.suvorov.server.commands;

import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;

import ru.suvorov.model.City;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.db.DBConnector;
import ru.suvorov.server.db.DBManager;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.builder.ConcreateCoordinatesBuilder;
import ru.suvorov.server.builder.ConcreateHumanBuilder;
import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.builder.DirectorBuilder;

import java.util.ArrayList;
import java.util.List;

public class GenerateRandomObj extends Command implements Executable {
    private final CollectionManager collectionManager;
    private final ConcreteCityBuilder concreteCityBuilder;
    private final ConcreateHumanBuilder concreateHumanBuilder;
    private final ConcreateCoordinatesBuilder concreateCoordinatesBuilder;
    public static String argument;

    public GenerateRandomObj(CollectionManager collectionManager, ConcreateCoordinatesBuilder concreateCoordinatesBuilder, ConcreteCityBuilder concreteCityBuilder, ConcreateHumanBuilder concreateHumanBuilder) {
        super("generate_random_obj count", "добавить случайные элементы в коллекцию");
        this.collectionManager = collectionManager;
        this.concreateCoordinatesBuilder = concreateCoordinatesBuilder;
        this.concreteCityBuilder = concreteCityBuilder;
        this.concreateHumanBuilder = concreateHumanBuilder;
    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        if (arg == null || !(arg instanceof String)) {
            return new ExecutionResponse(false, "Не указано количество объектов");
        }
        int count;
        try {
            count = Integer.parseInt(((String) arg).trim());
            argument = String.valueOf(count);
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "Аргумент должен быть числом");
        }
        if (count <= 0 || count > 1000) {
            return new ExecutionResponse(false, "Количество объектов должно быть от 1 до 1000");
        }
        String username = commandRequest.getUsername();
        if (username == null || username.isEmpty()) {
            return new ExecutionResponse(false, "Ошибка: логин пользователя не указан");
        }
        if (count > 10) {
            // Batch insert
            List<City> cities = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                City city = generateRandomCity();
                city.setOwnerUsername(username);
                cities.add(city);
            }
            // Используем batch insert
            new DBManager(new DBConnector()).addElementsBatch(cities, username);
            return new ExecutionResponse(true, "Добавлено (batch) " + count + " случайных объектов");
        } else {
            // Обычная вставка
        for (int i = 0; i < count; i++) {
            City city = generateRandomCity();
                city.setOwnerUsername(username);
            collectionManager.add(city);
            }
            return new ExecutionResponse(true, "Добавлено " + count + " случайных объектов");
        }
    }

    private City generateRandomCity() {
        DirectorBuilder.constructCityBuilder(concreteCityBuilder);
        DirectorBuilder.constructHumanBuilder(concreateHumanBuilder);
        DirectorBuilder.constructCoordinatesBuilder(concreateCoordinatesBuilder);

        concreteCityBuilder.setCoordinates(concreateCoordinatesBuilder.getCoordinates());
        concreteCityBuilder.setGovernor(concreateHumanBuilder.getHuman());

        return concreteCityBuilder.getResult();
    }
}