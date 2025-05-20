package ru.suvorov.utilities.collection.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.CollectionElement;
import ru.suvorov.server.util.Console;
import ru.suvorov.server.collection.ReadObject;
import ru.suvorov.server.collection.model.City;

public class UpdateId extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;
    private final CollectionElement collectionElement;

    public UpdateId(Console console, CollectionManager collectionManager, CollectionElement collectionElement) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.console = console;
        this.collectionManager = collectionManager;
        this.collectionElement = collectionElement;
    }

    @Override
    public ExecutionResponse apply(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) return new ExecutionResponse(false, "Необходимо указать id элемента.");
        Long idToUpdate;
        try {
            idToUpdate = Long.parseLong(arg.trim());
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "Неверный формат id: " + arg);
        }

        City cityId = collectionManager.byId(idToUpdate);
        if (cityId == null) return new ExecutionResponse(false, "Город с id " + idToUpdate + " не найден.");

        console.println("Введите новые данные для города с id " + idToUpdate + ":");

        City newCity = ReadObject.readCity(console, collectionElement);

        if (newCity == null) return new ExecutionResponse(false, "Ошибка: не удалось считать новые данные города.");
        newCity.setId();
        boolean success = collectionManager.update(newCity);

        if (success) {
            return new ExecutionResponse(true, "Город с id " + idToUpdate + " успешно обновлён.");
        } else
            return new ExecutionResponse(false, "Ошибка при обновлении города.");

    }



}
