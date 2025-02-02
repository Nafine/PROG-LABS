package se.ifmo.client.console;

import java.util.Scanner;

public interface Console {
    void print(Object obj);
    void println(Object obj);
    String readln();
    boolean ready();
    void printErr(Object obj);
    void printTable(Object obj1, Object obj2);
    void prompt();
    String getPrompt();
    void setFileScanner(Scanner scanner);
    void setConsoleScanner();
}
