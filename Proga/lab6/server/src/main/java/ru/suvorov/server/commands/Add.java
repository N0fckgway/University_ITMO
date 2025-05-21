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

public class Add extends Command implements Executable {
    private final Console console;
    private final CollectionManager collectionManager;
    private final CollectionElement collectionElement;

    public Add(Console console, CollectionManager collectionManager, CollectionElement collectionElement) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
        this.collectionElement = collectionElement;
    }

    @Override
    public ExecutionResponse apply(Object arg) {
        try {
            if (arg == null) {
                return new ExecutionResponse(false, "Ошибка: аргумент не может быть null");
            }
            
            if (!(arg instanceof City)) {
                return new ExecutionResponse(false, "Ошибка: ожидался объект City, получен " + arg.getClass().getName());
            }
            
            City city = (City) arg;
            try {
                city.validate();
            } catch (InvalidDataException e) {
                return new ExecutionResponse(false, "Ошибка валидации: " + e.getMessage());
            }
            
            if (collectionManager.add(city)) {
                return new ExecutionResponse(true, "Город успешно добавлен!");
            } else {
                return new ExecutionResponse(false, "Не удалось добавить город");
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при добавлении города: " + e.getMessage());
        }
    }
}
