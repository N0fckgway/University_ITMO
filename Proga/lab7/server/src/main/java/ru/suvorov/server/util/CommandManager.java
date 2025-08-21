package ru.suvorov.server.util;

import ru.suvorov.server.managers.Command;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final List<Command> commandHistory = new ArrayList<>();



    /**
     * Добавляет команду.
     * @param commandName Название команды.
     * @param command Команда.
     */
    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * @return Словарь команд.
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * @return История команд.
     */
    public List<Command> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Добавляет команду в историю.
     * @param command Команда.
     */
    public void addToHistory(Command command) {
        commandHistory.add(command);
    }

    public String executeCommand(String name, Command command) {
        register(name, command);
        return name;
    }

}
