package ru.suvorov.client.util;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class StadartConsole {
    private static final String firstSymbol = "> ";
    private static final Scanner scanner = new Scanner(System.in);

    public void print(Object obj) {
        System.out.print(obj);
    }

    public void println(Object obj) {
        System.out.println(obj);
    }

    public String readln() throws NoSuchElementException, IllegalStateException {
        print(firstSymbol);
        return scanner.nextLine();
    }

    public void printError(Object obj) {
        System.err.print("Err " + obj);
    }
} 