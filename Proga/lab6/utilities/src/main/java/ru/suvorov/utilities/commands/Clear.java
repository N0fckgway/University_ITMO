package ru.suvorov.utilities.collection.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.utilities.collection.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

public class Clear extends Command implements Executable {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String arg) {
        collectionManager.getCollection().clear();
        collectionManager.saveCollection();
        return new ExecutionResponse(true, "Коллекция очищена.");
    }
}
