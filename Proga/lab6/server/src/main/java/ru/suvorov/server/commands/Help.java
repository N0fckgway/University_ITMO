package ru.suvorov.server.commands;

import ru.suvorov.ExecutionResponse;

import ru.suvorov.server.builder.ConcreateCoordinatesBuilder;
import ru.suvorov.server.builder.ConcreateHumanBuilder;
import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.commands.interfaces.Executable;

import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

import ru.suvorov.server.util.CommandManager;
import ru.suvorov.server.util.Console;
import ru.suvorov.util.CollectionElement;

public class Help extends Command implements Executable {
    private final Console console;
    private final CommandManager command;
    private final CollectionManager collectionManager;
    private final CollectionElement collectionElement;


    // Конструктор принимает Console и CommandManager
    public Help(Console console, CommandManager command, CollectionManager collectionManager, CollectionElement collectionElement) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.command = command;
        this.collectionManager = collectionManager;
        this.collectionElement = collectionElement;
    }

    @Override
    public ExecutionResponse apply(Object arg) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("Доступные команды:\n");
        ConcreteCityBuilder concreteCityBuilder = new ConcreteCityBuilder();
        ConcreateHumanBuilder concreateHumanBuilder = new ConcreateHumanBuilder();
        ConcreateCoordinatesBuilder concreateCoordinatesBuilder = new ConcreateCoordinatesBuilder();
        command.register("add", new Add(console, collectionManager, collectionElement));
        command.register("add_if_min", new AddIfMin(console, collectionManager, collectionElement));
        command.register("clear", new Clear(collectionManager));
        command.register("exit", new Exit());
        command.register("execute_script", new ExecuteScript(console)); // передаём Runner
        command.register("filter_less_than_governor", new FilterLessThanGovernor(console, collectionManager));
        command.register("generate_random_obj", new GenerateRandomObj(collectionManager, concreateCoordinatesBuilder, concreteCityBuilder, concreateHumanBuilder));
        command.register("info", new Info(console, collectionManager));
        command.register("print_ascending", new PrintAscending(console, collectionManager));
        command.register("remove_by_id", new RemoveById(console, collectionManager));
        command.register("remove_first", new RemoveFirst(collectionManager));
        command.register("remove_greater", new RemoveGreater(console, collectionManager, collectionElement));
        command.register("save", new Save(collectionManager));
        command.register("show", new Show(console, collectionManager));
        command.register("sum_of_meters_above_sea_level", new SumOfMetersAboveSeaLevel(collectionManager));
        command.register("update_id", new UpdateId(console, collectionManager, collectionElement));

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
