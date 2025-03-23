package se.ifmo.client;

import se.ifmo.client.command.ClientSideCommands;
import se.ifmo.client.io.console.Console;
import se.ifmo.client.io.console.StandardConsole;
import se.ifmo.client.util.EnvManager;
import se.ifmo.shared.command.ExecuteScript;
import se.ifmo.shared.command.Exit;
import se.ifmo.shared.command.Help;

public class ClientMain {
    static {
        ClientSideCommands.registerCommand(new Exit());
        ClientSideCommands.registerCommand(new ExecuteScript());
        ClientSideCommands.registerCommand(new Help());
    }

    public static void main(String[] args) {
        try (Console console = new StandardConsole();
             Client client = new Client(console, EnvManager.getHost(),EnvManager.getPort())) {
            System.out.println("Client initiated");
            client.run();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
