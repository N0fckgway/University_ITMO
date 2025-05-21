package ru.suvorov.client.util;

import java.util.NoSuchElementException;
import java.util.Scanner;


public class StadartConsole implements Console {
    public static Scanner fileScanner = null;
    public static final String firstSymbol = "> ";
    public static  Scanner scanner = new Scanner(System.in);

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
        print(firstSymbol);
        return (fileScanner != null ? fileScanner : scanner).nextLine();
    }

    @Override
    public boolean isCanReadln() throws NoSuchElementException {
        return (fileScanner != null ? fileScanner : scanner).hasNextLine();
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
