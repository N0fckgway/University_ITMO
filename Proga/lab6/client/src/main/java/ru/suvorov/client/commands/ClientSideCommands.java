package ru.suvorov.client.commands;

import java.util.HashMap;

public class ClientSideCommands extends Command{

    public static final HashMap<String, Command> MAP = new HashMap<>();

    public ClientSideCommands(String name, Object argument) {
        super(name, argument);
    }
    public ClientSideCommands(){


    }

    public static Command registerCommand(Command command) {
        MAP.put(command.getName(), command);
        return command;
    }
}