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
import ru.suvorov.exception.InvalidDataException;

public class Add extends Command implements Executable {
    private final DBManager dbManager;


    public Add(DBManager dbManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.dbManager = dbManager;
    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {

        try {
            if (arg == null) {
                return new ExecutionResponse(false, "Ошибка: аргумент не может быть null");
            }

            if (!(arg instanceof City city)) {
                return new ExecutionResponse(false, "Ошибка: ожидался объект City, получен " + arg.getClass().getName());
            }

            try {
                city.validate();
            } catch (InvalidDataException e) {
                return new ExecutionResponse(false, "Ошибка валидации: " + e.getMessage());
            }
            String username = commandRequest.getUsername();
            if (username == null || username.isEmpty()) {
                return new ExecutionResponse(false, "Ошибка: логин пользователя не указан");
            }

             dbManager.addElement(city, username);
                return new ExecutionResponse(true, "Город успешно добавлен!");

        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при добавлении города: " + e.getMessage());
        }
    }
}
