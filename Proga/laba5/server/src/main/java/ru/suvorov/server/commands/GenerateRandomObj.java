package ru.suvorov.server.commands;

import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.builder.ConcreateCoordinatesBuilder;
import ru.suvorov.server.builder.ConcreateHumanBuilder;
import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.builder.DirectorBuilder;
import ru.suvorov.server.collection.model.City;

public class GenerateRandomObj extends Command implements Executable {
    private final CollectionManager collectionManager;
    private final ConcreteCityBuilder concreteCityBuilder;
    private final ConcreateHumanBuilder concreateHumanBuilder;
    private final ConcreateCoordinatesBuilder concreateCoordinatesBuilder;

    public GenerateRandomObj(CollectionManager collectionManager, ConcreateCoordinatesBuilder concreateCoordinatesBuilder, ConcreteCityBuilder concreteCityBuilder, ConcreateHumanBuilder concreateHumanBuilder) {
        super("generate_random_obj count", "добавить случайные элементы в коллекцию");
        this.collectionManager = collectionManager;
        this.concreateCoordinatesBuilder = concreateCoordinatesBuilder;
        this.concreteCityBuilder = concreteCityBuilder;
        this.concreateHumanBuilder = concreateHumanBuilder;
    }

    @Override
    public ExecutionResponse apply(String arg) {
        if (arg == null || arg.isEmpty()) {
            return new ExecutionResponse(false, "Не указано количество элементов");
        }
        int count;
        try {
            count = Integer.parseInt(arg.trim());
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "Формат кол-во команд неверен");
        }

        if (count <= 0 || count > 100) {
            return new ExecutionResponse(false, "Количество объектов должно быть от 1 до 100");
        }

        for (int i = 0; i < count; i++) {
            City city = generateRandomCity();
            collectionManager.add(city);
        }
        return new ExecutionResponse(true, "Добавлено " + count + " случайных объектов");
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