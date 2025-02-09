package se.ifmo;

import se.ifmo.client.communication.Handler;
import se.ifmo.client.console.Console;
import se.ifmo.client.console.StandardConsole;

public class Main {
    public static void main(String[] args) {
        try (Console console = new StandardConsole()) {
            new Handler(console).run();
        } catch (Exception e) {
            System.err.println("Error occured");
            System.err.println(e.getMessage());
        }
    }
}