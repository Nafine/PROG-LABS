package se.ifmo.client;

import se.ifmo.client.command.ClientSideCommands;
import se.ifmo.shared.io.console.Console;
import se.ifmo.shared.io.console.StandardConsole;
import se.ifmo.client.util.EnvManager;
import se.ifmo.shared.command.ExecuteScript;
import se.ifmo.shared.command.Exit;
import se.ifmo.shared.command.Help;

/**
 * Class which used as an entry point for server side of the program.
 * Registers client-side commands, creates console for user and launches client.
 */
public class ClientMain {
    static {
        ClientSideCommands.registerCommand(new Exit());
        ClientSideCommands.registerCommand(new ExecuteScript());
        ClientSideCommands.registerCommand(new Help());
    }

    public static void main(String[] args) {
        try (Console console = new StandardConsole();
             Client client = new Client(console, EnvManager.getHost(), EnvManager.getPort())) {
            System.out.printf("Client initiated, destination %s:%s%n",EnvManager.getHost().getHostAddress(), EnvManager.getPort());
            client.run();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
