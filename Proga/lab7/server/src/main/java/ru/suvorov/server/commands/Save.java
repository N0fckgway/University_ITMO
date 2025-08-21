package ru.suvorov.server.commands;



import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;

import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

public class Save extends Command implements Executable {


    public Save() {
        super("save", "сохранить коллекцию в файл. (UPD: коллекцию уже хранится в базе данных)");

    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        return new ExecutionResponse(true, "Коллекция уже хранится в базе данных. Сохранять вручную не требуется. ");
    }
}
