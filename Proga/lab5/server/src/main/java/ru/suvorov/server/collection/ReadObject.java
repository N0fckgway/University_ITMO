package ru.suvorov.server.collection;

import ru.suvorov.common.exception.InvalidDataException;
import ru.suvorov.common.util.CollectionElement;
import ru.suvorov.common.util.Console;

import ru.suvorov.server.collection.enums.Climate;
import ru.suvorov.server.collection.enums.Government;
import ru.suvorov.server.collection.enums.StandardOfLiving;
import ru.suvorov.server.collection.model.City;
import ru.suvorov.server.collection.model.Coordinates;
import ru.suvorov.server.collection.model.Human;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

public class ReadObject {

    public static City readCity(Console console, CollectionElement collectionElement) throws Exception {
        try {
            String name = readName(console, "Name: ");
            int area = readArea(console, "Area: ");
            long population = readPopulation(console, "Population: ");
            double metersAboveSeaLevel = readMetersAboveSeaLevel(console, "MetersAboveSeaLevel: ");
            ZonedDateTime creationDate = readCreationDate(console, "CreationDate: Example: 2025-12-25T00:00:00+03:00[Europe/Moscow] (or 'exit' to quit): ");
            Coordinates coordinates = readCoordinates(console);
            Human governor = readHuman(console);
            Climate climate = readClimate(console);
            Government government = readGovernment(console);
            StandardOfLiving standardOfLiving = readStandardOfLiving(console);


            return new City(collectionElement.setId(), name, coordinates, collectionElement.setDate(), area, population, metersAboveSeaLevel, climate, government, standardOfLiving, governor);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    private static String readName(Console console, String prompt) throws Exception {
        while (true) {
            console.print(prompt);
            String input = console.readln().trim();
            if (input.equals("exit")) throw new Exception();
            if (!input.isEmpty()) return input;
            console.printError("Поле не может быть пустым");
        }
    }

    private static int readArea(Console console, String prompt) throws Exception {
        while (true) {
            console.print(prompt);
            String input = console.readln().trim();
            if (input.equals("exit")) throw new Exception();
            if (!input.isEmpty()) {
                try {
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    console.printError("Неверный формат числа");
                }
            }
        }
    }

    private static long readPopulation(Console console, String prompt) throws Exception {
        while (true) {
            console.print(prompt);
            String input = console.readln().trim();
            if (input.equals("exit")) throw new Exception();
            if (!input.isEmpty()) {
                try {
                    return Long.parseLong(input);
                } catch (NumberFormatException e) {
                    console.printError("Неверный формат числа");
                }
            }
        }
    }

    private static double readMetersAboveSeaLevel(Console console, String prompt) throws Exception {
        while (true) {
            console.print(prompt);
            String input = console.readln().trim();
            if (input.equals("exit")) throw new Exception();
            if (!input.isEmpty()) {
                try {
                    return Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    console.printError("Неверный формат числа");
                }
            }
        }
    }

    private static ZonedDateTime readCreationDate(Console console, String prompt) throws Exception {
        while (true) {
            console.print(prompt);
            String input = console.readln().trim();
            if (input.equals("exit")) throw new Exception();
            if (input.isEmpty()) return null;
            try {
                return ZonedDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeParseException e) {
                console.printError("Неверный формат даты");
            }
        }
    }

    public static Coordinates readCoordinates(Console console) throws Exception {
        try {
            long x = readPopulation(console, "Coordinate x: ");
            long y = readPopulation(console, "Coordinate y: ");
            if (y > -583) throw new InvalidDataException(y, "Координата y неверно указана");
            return new Coordinates(x, y);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Human readHuman(Console console) throws Exception {
        try {
            String name = readName(console, "Human.Name: ");
            int age = readArea(console, "Human.Age: ");
            float height = (float) readMetersAboveSeaLevel(console, "Human.height: ");
            ZonedDateTime birthday = readCreationDate(console, "Human.birth: Example: 2025-12-25T00:00:00+03:00[Europe/Moscow] (or 'exit' to quit): ");
            if (age > 0) throw new InvalidDataException(age, "Возраст > 0");
            if (height > 0) throw new InvalidDataException(height, "Рост > 0");
            return new Human(name, age, height, birthday);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Climate readClimate(Console console) throws Exception {
        try {
            while (true) {
                console.print("Climate:  ");
                String input = console.readln().trim();
                if (input.equals("exit")) throw new Exception();
                if (!input.isEmpty()) {
                    try {
                        return Climate.valueOf(input);
                    } catch (IllegalArgumentException e) {
                        console.printError("Неверный тип климата");
                    }
                }
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Government readGovernment(Console console) throws Exception {
        try {
            while (true) {
                console.print("Government: ");
                String input = console.readln().trim();
                if (input.equals("exit")) throw new Exception();
                if (!input.isEmpty()) {
                    try {
                        return Government.valueOf(input);
                    } catch (IllegalArgumentException e) {
                        console.printError("Неверный тип правительства");
                    }
                }
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static StandardOfLiving readStandardOfLiving(Console console) throws Exception {
        try {
            while (true) {
                console.print("StandardOfLiving: ");
                String input = console.readln().trim();
                if (input.equals("exit")) throw new Exception();
                if (!input.isEmpty()) {
                    try {
                        return StandardOfLiving.valueOf(input);
                    } catch (IllegalArgumentException e) {
                        console.printError("Неверный уровень жизни");
                    }
                }
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
}