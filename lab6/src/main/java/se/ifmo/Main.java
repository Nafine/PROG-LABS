package se.ifmo;

import se.ifmo.client.console.Console;
import se.ifmo.client.console.StandardConsole;
import se.ifmo.shared.communication.Handler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (Console console = new StandardConsole()) {
            new Handler(console).run();
        } catch (IOException e) {
            System.err.println("Can't find");
        } catch (Exception e) {
            System.err.println("Error occurred.");
            System.err.println(e.getMessage());
        }
    }
}