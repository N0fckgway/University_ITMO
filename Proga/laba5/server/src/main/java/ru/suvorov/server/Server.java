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


        Runner runner = new Runner(console, collectionManager, collectionElement);
        runner.run();


    }
}


