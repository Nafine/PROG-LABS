package se.ifmo;

import se.ifmo.client.communication.Handler;
import se.ifmo.client.communication.exceptions.ProgramFinishedException;
import se.ifmo.client.console.Console;
import se.ifmo.client.console.StandardConsole;
import se.ifmo.system.collection.util.IdGenerator;

public class Main {
    public static void main(String[] args) {
        try (Console console = new StandardConsole()) {
            new Handler(console).run();
        } catch (ProgramFinishedException e) {
            System.out.println("Program finished");
        } catch (Exception e) {
            System.err.println("Error occurred.");
            System.err.println(e.getMessage());
        }
    }
}