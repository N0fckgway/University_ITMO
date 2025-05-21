package ru.suvorov.server.managers;


import ru.suvorov.ExecutionResponse;
import ru.suvorov.server.builder.ConcreateCoordinatesBuilder;
import ru.suvorov.server.builder.ConcreateHumanBuilder;
import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.commands.*;
import ru.suvorov.server.commands.interfaces.Executable;

import ru.suvorov.server.util.CommandManager;
import ru.suvorov.server.util.Console;
import ru.suvorov.util.CollectionElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Runner {
    private final Map<String, Executable> commands = new HashMap<>();
    private final Console console;
    private final CollectionManager collectionManager;
    private final CollectionElement collectionElement;
    private final Set<String> scriptStack = new HashSet<>();

    public Runner(Console console, CollectionManager collectionManager, CollectionElement collectionElement) {
        this.console = console;
        this.collectionManager = collectionManager;
        this.collectionElement = collectionElement;
        registerCommands();
    }

    private void registerCommands() {
        ConcreteCityBuilder concreteCityBuilder = new ConcreteCityBuilder();
        ConcreateCoordinatesBuilder concreateCoordinatesBuilder = new ConcreateCoordinatesBuilder();
        ConcreateHumanBuilder concreateHumanBuilder = new ConcreateHumanBuilder();
        CommandManager commandManager = new CommandManager();
        commands.put("add", new Add(console, collectionManager, collectionElement));
        commands.put("add_if_min", new AddIfMin(console, collectionManager, collectionElement));
        commands.put("clear", new Clear(collectionManager));
        commands.put("exit", new Exit());
        commands.put("execute_script", new ExecuteScript(console)); // передаём Runner
        commands.put("filter_less_than_governor", new FilterLessThanGovernor(console, collectionManager));
        commands.put("generate_random_obj", new GenerateRandomObj(collectionManager, concreateCoordinatesBuilder, concreteCityBuilder, concreateHumanBuilder));
        commands.put("help", new Help(console, commandManager, collectionManager, collectionElement));
        commands.put("info", new Info(console, collectionManager));
        commands.put("print_ascending", new PrintAscending(console, collectionManager));
        commands.put("remove_by_id", new RemoveById(console, collectionManager));
        commands.put("remove_first", new RemoveFirst(collectionManager));
        commands.put("remove_greater", new RemoveGreater(console, collectionManager, collectionElement));
        commands.put("save", new Save(collectionManager));
        commands.put("show", new Show(console, collectionManager));
        commands.put("sum_of_meters_above_sea_level", new SumOfMetersAboveSeaLevel(collectionManager));
        commands.put("update_id", new UpdateId(console, collectionManager, collectionElement));
    }

    public void run() {
        console.println("Введите команду:");
        Scanner scanner = new Scanner(System.in);
        processInput(scanner);
    }

    public void processInput(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String commandName = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";

            Executable command = commands.get(commandName.toLowerCase());

            if (command != null) {
                try {
                    ExecutionResponse response = command.apply(arg);
                    console.println(response.getMessage());
                } catch (Exception e) {
                    console.println("Ошибка при выполнении команды: " + e.getMessage());
                }
            } else {
                console.println("Неизвестная команда: " + commandName);
            }

            console.println("\nВведите следующую команду:");
        }
    }

    public void executeScriptFromFile(String fileName) {
        if (scriptStack.contains(fileName)) {
            console.println("Скрипт уже выполняется (рекурсия): " + fileName);
            return;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            console.println("Файл не найден: " + fileName);
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            scriptStack.add(fileName);
            processInput(fileScanner);
            scriptStack.remove(fileName);
        } catch (FileNotFoundException e) {
            console.println("Не удалось открыть файл скрипта: " + fileName);
        }
    }
}