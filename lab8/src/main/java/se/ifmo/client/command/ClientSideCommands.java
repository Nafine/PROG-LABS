package se.ifmo.client.command;

import se.ifmo.shared.command.Command;

import java.util.HashMap;

/**
 * Class which contains all registered commands of the client.
 * <p>
 * All registered command can be used in console by user, they handle all type of exceptions that
 * can occur during their execution time.
 * </p>
 */
public class ClientSideCommands {
    /**
     * Public static final field with list of all available commands.
     */
    public static final HashMap<String, Command> MAP = new HashMap<>();

    private ClientSideCommands() {}

    public static void registerCommand(Command command) {
        MAP.put(command.getName(), command);
    }
}
