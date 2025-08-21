package ru.suvorov.server.util;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class StadartConsole implements Console {
    private static final String firstSymbol = "> ";
    private static final Scanner scanner = new Scanner(System.in);

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
        return scanner.nextLine();
    }

    @Override
    public void printError(Object obj) {
        System.err.print("Err " + obj);
    }
}
