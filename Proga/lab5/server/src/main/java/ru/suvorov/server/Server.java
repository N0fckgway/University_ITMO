package ru.suvorov.server;


import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.DumpManager;
import ru.suvorov.server.managers.Runner;
import ru.suvorov.server.util.CollectionElement;

import ru.suvorov.server.util.StadartConsole;

public final class Server {

    public static void main(String[] args) {
        StadartConsole console = new StadartConsole();

        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }


        DumpManager dumpManager = new DumpManager(args[0], console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);
        CollectionElement collectionElement = collectionManager.getCollectionElement();
        if (!collectionManager.init()) {
            console.printError("Ошибка при инициализации коллекции");
            System.exit(1);
        }


//
//        command.register("info", new Info(console, collectionManager));
//        command.register("show", new Show(console, collectionManager))
//        command.register("update id", new UpdateId(console, collectionManager, collectionElement));
//        command.register("remove_by_id", new RemoveById(console, collectionManager));
//        command.register("clear", new Clear(collectionManager));
//        command.register("save", new Save(collectionManager));
//        command.register("execute_script", new ExecuteScript(console));
//        command.register("exit", new Exit());
//        command.register("remove_first", new RemoveFirst(collectionManager));
//        command.register("add_if_min", new AddIfMin(console, collectionManager, collectionElement));
//        command.register("remove_greater", new RemoveGreater(console, collectionManager, collectionElement));
//        command.register("sum_of_meters_above_sea_level", new SumOfMetersAboveSeaLevel(collectionManager));
//        command.register("filter_less_than_governor", new FilterLessThanGovernor(console, collectionManager));
//        command.register("print_ascending", new PrintAscending(console, collectionManager));
//        command.register("generate", new GenerateRandomObj(collectionManager, new ConcreateCoordinatesBuilder(), new ConcreteCityBuilder(), new ConcreateHumanBuilder()));
//


        Runner runner = new Runner(console, collectionManager, collectionElement);
        runner.run();



    }
}


