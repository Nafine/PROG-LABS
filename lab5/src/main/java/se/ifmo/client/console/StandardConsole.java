package se.ifmo.client.console;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class StandardConsole implements Console {
    public static String P = "$ ";
    private static Scanner fileScanner;
    private static final Scanner defScanner = new Scanner(System.in);

    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    @Override
    public void printErr(Object obj) {
        System.err.println("Error: " + obj);
    }

    @Override
    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).nextLine();
    }

    @Override
    public boolean ready() throws IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).hasNextLine();
    }

    @Override
    public void printTable(Object left, Object right) {
        System.out.printf(" %-35s%-1s%n", left, right);
    }

    @Override
    public void prompt() {
        print(P);
    }

    @Override
    public String getPrompt() {
        return P;
    }

    @Override
    public void setFileScanner(Scanner fileScanner) {
        StandardConsole.fileScanner = fileScanner;
    }

    @Override
    public void setConsoleScanner() {
        fileScanner = null;
    }
}
