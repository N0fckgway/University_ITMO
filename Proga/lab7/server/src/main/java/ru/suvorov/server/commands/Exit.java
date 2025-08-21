package ru.suvorov.server.commands;


import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.Command;

public class Exit extends Command implements Executable {
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        System.exit(0);
        return new ExecutionResponse(true, "Программа завершена.");
    }
}
