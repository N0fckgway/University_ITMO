package ru.suvorov.server.commands;

import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.managers.Runner;
import ru.suvorov.server.util.Console;

public class ExecuteScript extends Command implements Executable {

    private final Console console;
    private final Runner runner;

    public ExecuteScript(Console console, Runner runner) {
        super("execute_script", "выполнить скрипт из файла");
        this.console = console;
        this.runner = runner;
    }

    @Override
    public ExecutionResponse apply(String arg) {
        return runner != null
                ? runner.scriptMode(arg)
                : new ExecutionResponse(false, "Ошибка: Runner не передан в команду execute_script");
    }
}