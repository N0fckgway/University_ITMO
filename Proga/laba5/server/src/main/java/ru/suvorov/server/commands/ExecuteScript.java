package ru.suvorov.server.commands;


import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.Console;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ExecuteScript extends Command implements Executable {
    private final Console console;

    public ExecuteScript(Console console) {
        super("execute_script file_name :", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n");
        this.console = console;
    }

    @Override
    public ExecutionResponse apply(String arg) throws Exception {
        if (arg == null || arg.isEmpty()) {
            return new ExecutionResponse(false, "Не указан файл скрипта");
        }

        String fileName = arg.trim();
        File file = new File(fileName);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();


                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    console.println("Выполняется команда: " + line);

                } catch (Exception e) {
                    console.println("Ошибка при выполнении команды: " + line);
                    return new ExecutionResponse(false, "Ошибка выполнения команды в скрипте: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            return new ExecutionResponse(false, "Файл не найден: " + e.getMessage());
        }

        return new ExecutionResponse(true, "Скрипт успешно выполнен");
    }


}
