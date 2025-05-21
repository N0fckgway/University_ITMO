package ru.suvorov.client.util;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class StadartConsole implements Console {
    public static Scanner fileScanner = null;
    public static final String firstSymbol = "> ";
    private static final Scanner scanner = new Scanner(System.in, "UTF-8");

    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    @Override
    public String readln() throws NoSuchElementException, IllegalStateException {
        if (fileScanner != null) {
            print(firstSymbol);
            return fileScanner.nextLine();
        }
        try {
            print(firstSymbol);
            String line = scanner.nextLine();
            if (line == null || line.trim().isEmpty()) {
                return "";
            }
            return line;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                throw (NoSuchElementException) e;
            }
            throw new NoSuchElementException("Ошибка чтения ввода: " + e.getMessage());
        }
    }

    @Override
    public boolean isCanReadln() throws NoSuchElementException {
        if (fileScanner != null) {
            return fileScanner.hasNextLine();
        }
        return true; // Для консольного ввода всегда возвращаем true
    }

    @Override
    public void printError(Object obj) {
        System.err.print("Err " + obj);
    }

    @Override
    public void printTable(Object obj1, Object obj2) {
        System.out.printf(" %-35s%-1s%n", obj1, obj2);
    }

    @Override
    public void prompt() {
        print(firstSymbol);
    }

    @Override
    public String getPrompt() {
        return firstSymbol;
    }

    @Override
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    @Override
    public void selectConsoleScanner() {
        fileScanner = null;
    }
}
