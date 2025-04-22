package ru.suvorov.server.managers;

import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.builder.ConcreateCoordinatesBuilder;
import ru.suvorov.server.builder.ConcreateHumanBuilder;
import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.commands.*;
import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.util.CollectionElement;
import ru.suvorov.server.util.CommandManager;
import ru.suvorov.server.util.Console;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Runner {
    private final Map<String, Executable> commands = new HashMap<>();
    private Console console;
    private CollectionManager collectionManager;
    private CollectionElement collectionElement;
    private final Set<String> scriptStack = new HashSet<>();

    public Runner(Console console, CollectionManager collectionManager, CollectionElement collectionElement) {
        this.console = console;
        this.collectionManager = collectionManager;
        this.collectionElement = collectionElement;
        registerCommands();
    }

    public Runner() {

    }



    private void registerCommands() {
        CommandManager commandManager = new CommandManager();
        ConcreateHumanBuilder concreateHumanBuilder = new ConcreateHumanBuilder();
        ConcreteCityBuilder concreteCityBuilder = new ConcreteCityBuilder();
        ConcreateCoordinatesBuilder concreateCoordinatesBuilder = new ConcreateCoordinatesBuilder();

        commands.put("add", new Add(console, collectionManager, collectionElement));
        commands.put("add_if_min", new AddIfMin(console, collectionManager, collectionElement));
        commands.put("clear", new Clear(collectionManager));
        commands.put("exit", new Exit());
        commands.put("execute_script", new ExecuteScript(console, this));
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

    private boolean checkRecursion(String filename) {
        if (scriptStack.contains(filename)) {
            return true;
        } else {
            scriptStack.add(filename);
            return false;
        }
    }

    public ExecutionResponse scriptMode(String argument) {
        String[] userCommand;
        StringBuilder executionOutput = new StringBuilder();

        if (!new File(argument).exists()) return new ExecutionResponse(false, "Файл не существует!");
        if (!Files.isReadable(Paths.get(argument))) return new ExecutionResponse(false, "Нет прав на чтение файла!");

        if (checkRecursion(argument)) {
            return new ExecutionResponse(false, "Рекурсивный вызов скрипта: " + argument);
        }

        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);

            while (scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                userCommand = (line + " ").split(" ", 2);
                userCommand[1] = userCommand.length > 1 ? userCommand[1].trim() : "";

                executionOutput.append(console.getPrompt()).append(String.join(" ", userCommand)).append("\n");

                ExecutionResponse response = launchCommand(userCommand);
                executionOutput.append(response.getMessage()).append("\n");

                if (!response.getExitCode()) break;
            }

            console.selectConsoleScanner();
            return new ExecutionResponse(true, executionOutput.toString());

        } catch (FileNotFoundException e) {
            return new ExecutionResponse(false, "Файл со скриптом не найден!");
        } catch (NoSuchElementException e) {
            return new ExecutionResponse(false, "Файл со скриптом пуст!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении скрипта: " + e.getMessage());
        } finally {
            scriptStack.remove(argument);
        }
    }

    private ExecutionResponse launchCommand(String[] userCommand) throws Exception {
        if (userCommand[0].isEmpty()) return new ExecutionResponse("");

        Executable command = commands.get(userCommand[0]);

        if (command == null) {
            return new ExecutionResponse(false, "Команда '" + userCommand[0] + "' не найдена.");
        }

        if (userCommand[0].equals("execute_script")) {
            return scriptMode(userCommand[1]);
        }

        return command.apply(userCommand[1]);
    }
}
