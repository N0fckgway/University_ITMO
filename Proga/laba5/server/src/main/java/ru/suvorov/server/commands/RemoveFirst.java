package ru.suvorov.server.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

public class RemoveFirst extends Command implements Executable {
    private final CollectionManager collectionManager;

    public RemoveFirst(CollectionManager collectionManager) {
        super("remove_first :", "удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
    }


    @Override
    public ExecutionResponse apply(String arg) {
        if (collectionManager.getCollection() == null || collectionManager.getCollection().isEmpty())
            return new ExecutionResponse(false, "Коллекция пуста или удаление невозможно");

        Long firstCityId = collectionManager.getCollection().getFirst().getId();
        boolean success = collectionManager.remove(firstCityId);

        if (success) {
            return new ExecutionResponse(true, "Первый элемент успешно удален");

        } else return new ExecutionResponse(false, "Ошибка удаления");

    }
}
