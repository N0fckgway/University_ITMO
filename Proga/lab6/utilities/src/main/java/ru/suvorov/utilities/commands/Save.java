package ru.suvorov.utilities.collection.commands;



import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.utilities.collection.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

public class Save extends Command implements Executable {
    private final CollectionManager collectionManager;

    public Save(CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String arg) {
        collectionManager.saveCollection();
        return new ExecutionResponse(true, "Коллекция сохранена в файл.");
    }
}
