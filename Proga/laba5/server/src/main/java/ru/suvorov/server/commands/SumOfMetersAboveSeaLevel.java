package ru.suvorov.server.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.collection.model.City;

import java.util.LinkedList;

public class SumOfMetersAboveSeaLevel extends Command implements Executable {
    private final CollectionManager collectionManager;

    public SumOfMetersAboveSeaLevel(CollectionManager collectionManager) {
        super("sum_of_meters_above_sea_level :", "вывести сумму значений поля metersAboveSeaLevel для всех элементов коллекции");
        this.collectionManager = collectionManager;

    }

    @Override
    public ExecutionResponse apply(String arg) {
        if (collectionManager.getCollection() == null || collectionManager.getCollection().isEmpty()) {
            return new ExecutionResponse(false, "Коллекция пуста или удаление невозможно");

        }

        LinkedList<City> newCity = collectionManager.getCollection();
        long sum = 0;

        for (City city : newCity) {
            sum += (long) city.getMetersAboveSeaLevel();
        }
        return new ExecutionResponse(true, "Сумма значений metersAboveSeaLevel: " + sum);


    }
}
