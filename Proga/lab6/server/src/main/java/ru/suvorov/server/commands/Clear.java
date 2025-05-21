package ru.suvorov.server.commands;


import ru.suvorov.ExecutionResponse;

import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

public class Clear extends Command implements Executable {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(Object arg) {
        collectionManager.getCollection().clear();
        collectionManager.saveCollection();
        return new ExecutionResponse(true, "Коллекция очищена.");
    }
}
