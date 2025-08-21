package ru.suvorov.server.managers;


import ru.suvorov.ExecutionResponse;
import ru.suvorov.server.builder.ConcreateCoordinatesBuilder;
import ru.suvorov.server.builder.ConcreateHumanBuilder;
import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.commands.*;
import ru.suvorov.server.commands.interfaces.Executable;

import ru.suvorov.server.db.DBConnector;
import ru.suvorov.server.db.DBManager;
import ru.suvorov.server.util.CommandManager;
import ru.suvorov.server.util.Console;
import ru.suvorov.server.util.StadartConsole;
import ru.suvorov.util.CollectionElement;

import java.util.*;

public class Runner {
    private final Map<String, Executable> commands = new HashMap<>();
    private final StadartConsole console;
    private final CollectionManager collectionManager;
    private final DBConnector dbConnector;
    private final DBManager dbManager;

    public Runner(StadartConsole console, CollectionManager collectionManager, DBConnector dbConnector, DBManager dbManager) {
        this.console = console;
        this.collectionManager = collectionManager;
        this.dbConnector = dbConnector;
        this.dbManager = dbManager;
        registerCommands();
    }

    private void registerCommands() {
        ConcreteCityBuilder concreteCityBuilder = new ConcreteCityBuilder();
        ConcreateCoordinatesBuilder concreateCoordinatesBuilder = new ConcreateCoordinatesBuilder();
        ConcreateHumanBuilder concreateHumanBuilder = new ConcreateHumanBuilder();
        CommandManager commandManager = new CommandManager();
        commands.put("add", new Add(dbManager));
        commands.put("add_if_min", new AddIfMin(console, collectionManager));
        commands.put("clear", new Clear(collectionManager));
        commands.put("exit", new Exit());
        commands.put("execute_script", new ExecuteScript(console)); // передаём Runner
        commands.put("filter_less_than_governor", new FilterLessThanGovernor(console, collectionManager));
        commands.put("generate_random_obj", new GenerateRandomObj(collectionManager, concreateCoordinatesBuilder, concreteCityBuilder, concreateHumanBuilder));
        commands.put("help", new Help(console, commandManager, collectionManager));
        commands.put("info", new Info(console, collectionManager));
        commands.put("print_ascending", new PrintAscending(console));
        //удалил пару методов
        commands.put("remove_greater", new RemoveGreater(console, collectionManager));
        commands.put("save", new Save());
        commands.put("show", new Show(console, collectionManager));
        commands.put("sum_of_meters_above_sea_level", new SumOfMetersAboveSeaLevel(collectionManager));
        commands.put("update_id", new UpdateId(collectionManager));
    }

    public Map<String, Executable> getCommands() {
        return commands;
    }
}