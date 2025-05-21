package ru.suvorov.server.commands;


import ru.suvorov.ExecutionResponse;
import ru.suvorov.model.City;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.Console;
import ru.suvorov.server.util.ReadObject;
import ru.suvorov.util.CollectionElement;
import ru.suvorov.exception.InvalidDataException;


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
    public ExecutionResponse apply(Object arg) throws Exception {
        try {
            if (!(arg instanceof Object[] arr) || arr.length != 2 || !(arr[0] instanceof Long) || !(arr[1] instanceof City)) {
                return new ExecutionResponse(false, "Ошибка: ожидался массив [Long id, City city].");
            }
            Long idToUpdate = (Long) arr[0];
            City newCity = (City) arr[1];
            newCity.validate();
            Long cityId = collectionManager.byId(idToUpdate);
            if (cityId == null) return new ExecutionResponse(false, "Город с id " + idToUpdate + " не найден.");
            boolean success = collectionManager.update(cityId, newCity);
            if (success) {
                return new ExecutionResponse(true, "Город с id " + idToUpdate + " успешно обновлён.");
            } else
                return new ExecutionResponse(false, "Ошибка при обновлении города.");
        } catch (InvalidDataException e) {
            return new ExecutionResponse(false, e.getMessage());
        }
    }



}
