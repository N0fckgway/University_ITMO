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


public class UpdateId extends Command implements Executable {
    private final CollectionManager collectionManager;


    public UpdateId(CollectionManager collectionManager) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;

    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        try {
            DBManager dbManager = new DBManager(new DBConnector());
            if (arg == null) {
                return new ExecutionResponse(false, "Ошибка: ожидался id");
            }
            dbManager.getCityById(Long.parseLong((String) arg)).validate();

            // Проверка существования города
            City existingCity = dbManager.getCityById(Long.parseLong((String) arg));
            String username = commandRequest.getUsername();
            if (existingCity == null) {
                return new ExecutionResponse(false, "Город с id " + arg + " не найден.");
            }

            dbManager.getCityById(Long.parseLong((String) arg)).setOwnerUsername(username);

            // Проверка владельца
            String owner = existingCity.getOwnerUsername();
            if (!commandRequest.getUsername().equals(owner)) {
                return new ExecutionResponse(false, "Вы не являетесь владельцем этого города.");
            }

            dbManager.updateCity(Long.parseLong((String) arg), existingCity);

            return new ExecutionResponse(true, "Город с id " + arg + " успешно обновлён.");
        } catch (InvalidDataException e) {
            return new ExecutionResponse(false, e.getMessage());
        }
    }



}
