package ru.suvorov.client.util;

import java.util.Scanner;

public interface Console {
    void print(Object obj);
    void println(Object obj);
    String readln();
    boolean isCanReadln();
    void printError(Object obj);
    void printTable(Object obj1, Object obj2);
    void prompt();
    String getPrompt();
    void selectFileScanner(Scanner obj1);
    void selectConsoleScanner();

}
