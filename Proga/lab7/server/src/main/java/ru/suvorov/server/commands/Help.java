package ru.suvorov.server.commands;

import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;

import ru.suvorov.server.builder.ConcreateCoordinatesBuilder;
import ru.suvorov.server.builder.ConcreateHumanBuilder;
import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.commands.interfaces.Executable;

import ru.suvorov.server.db.DBConnector;
import ru.suvorov.server.db.DBManager;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

import ru.suvorov.server.util.CommandManager;
import ru.suvorov.server.util.Console;
import ru.suvorov.util.CollectionElement;

public class Help extends Command implements Executable {
    private final Console console;
    private final CommandManager command;
    private final CollectionManager collectionManager;



    public Help(Console console, CommandManager command, CollectionManager collectionManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.command = command;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("Доступные команды:\n");
        ConcreteCityBuilder concreteCityBuilder = new ConcreteCityBuilder();
        ConcreateHumanBuilder concreateHumanBuilder = new ConcreateHumanBuilder();
        ConcreateCoordinatesBuilder concreateCoordinatesBuilder = new ConcreateCoordinatesBuilder();
        DBConnector dbConnector = new DBConnector();
        DBManager dbManager = new DBManager(dbConnector);
        command.register("add", new Add(dbManager));
        command.register("add_if_min", new AddIfMin(console, collectionManager));
        command.register("clear", new Clear(collectionManager));
        command.register("exit", new Exit());
        command.register("execute_script", new ExecuteScript(console)); // передаём Runner
        command.register("filter_less_than_governor", new FilterLessThanGovernor(console, collectionManager));
        command.register("generate_random_obj", new GenerateRandomObj(collectionManager, concreateCoordinatesBuilder, concreteCityBuilder, concreateHumanBuilder));
        command.register("info", new Info(console, collectionManager));
        command.register("print_ascending", new PrintAscending(console));
        command.register("remove_by_id", new RemoveById(console, collectionManager));
        //удалил один метод
        command.register("remove_greater", new RemoveGreater(console, collectionManager));
        command.register("save", new Save());
        command.register("show", new Show(console, collectionManager));
        command.register("sum_of_meters_above_sea_level", new SumOfMetersAboveSeaLevel(collectionManager));
        command.register("update_id", new UpdateId(collectionManager));

        for (Command e : command.getCommands().values()) {
            helpMessage.append(e.getName())
                    .append(" - ")
                    .append(e.getDescription())
                    .append("\n");
        }
        helpMessage.append("execute_script - выполнить команды из скрипта\n");
        console.print(helpMessage.toString());

        return new ExecutionResponse(true, "Справка по доступным командам выведена.");
    }
}
