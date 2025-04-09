package ru.suvorov.server.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.Command;

public class Exit extends Command implements Executable {
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    @Override
    public ExecutionResponse apply(String arg) {
        System.exit(0);
        return new ExecutionResponse(true, "Программа завершена.");
    }
}
