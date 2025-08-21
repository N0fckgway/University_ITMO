package ru.suvorov.server.commands;


import ru.suvorov.ExecutionResponse;
import ru.suvorov.server.Server;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.managers.Command;
import ru.suvorov.server.util.Console;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExecuteScript extends Command implements Executable {
    private final Console console;
    private static final Set<String> scriptStack = new HashSet<>();

    public ExecuteScript(Console console) {
        super("execute_script file_name :", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n");
        this.console = console;
    }

    @Override
public ExecutionResponse apply(Object arg) throws Exception {
    if (arg == null || ((String) arg).isEmpty()) {
        return new ExecutionResponse(false, "Не указан файл скрипта");
    }

    String fileName = ((String) arg).trim();
        if (scriptStack.contains(fileName)) {
            return new ExecutionResponse(false, "Обнаружена рекурсия при выполнении скрипта: " + fileName);
        }
    File file = new File(fileName);

    try (Scanner scanner = new Scanner(file)) {
            scriptStack.add(fileName);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            // Парсим команду и аргумент
            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0];
            String cmdArg = parts.length > 1 ? parts[1] : null;

            // Получаем команду из реестра
            Executable command = Server.getCommandRegistry().get(cmd);
            if (command != null) {
                ExecutionResponse resp = command.apply(cmdArg);
                console.println(resp.getMessage());
            } else {
                console.println("Неизвестная команда: " + cmd);
            }
        }
            scriptStack.remove(fileName);
    } catch (FileNotFoundException e) {
            scriptStack.remove(fileName);
        return new ExecutionResponse(false, "Файл не найден: " + e.getMessage());
        } catch (Exception e) {
            scriptStack.remove(fileName);
            throw e;
    }

    return new ExecutionResponse(true, "Скрипт успешно выполнен");
}


}
