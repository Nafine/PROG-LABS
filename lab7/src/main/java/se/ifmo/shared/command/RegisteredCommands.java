package se.ifmo.shared.command;

import java.util.HashMap;

/**
 * Class which contains all registered commands of the program.
 * <p>
 * All registered command can be used in console by user, they handle all type of exceptions that
 * can occur during their execution time.
 * </p>
 */
public class RegisteredCommands {
    /**
     * Public static final field with list of all available commands.
     */
    public static final HashMap<String, Command> MAP = new HashMap<>();

    static {
        RegisteredCommands.registerCommand(new Add());
        RegisteredCommands.registerCommand(new AddIfMin());
        RegisteredCommands.registerCommand(new AddRandom());
        RegisteredCommands.registerCommand(new Clear());
        RegisteredCommands.registerCommand(new ExecuteScript());
        RegisteredCommands.registerCommand(new Exit());
        RegisteredCommands.registerCommand(new FilterLessThanFuelType());
        RegisteredCommands.registerCommand(new Help());
        RegisteredCommands.registerCommand(new History());
        RegisteredCommands.registerCommand(new Info());
        RegisteredCommands.registerCommand(new Login());
        RegisteredCommands.registerCommand(new MinByCreationDate());
        RegisteredCommands.registerCommand(new Register());
        RegisteredCommands.registerCommand(new RemoveById());
        RegisteredCommands.registerCommand(new RemoveGreater());
        RegisteredCommands.registerCommand(new Show());
        RegisteredCommands.registerCommand(new SumOfEnginePower());
        RegisteredCommands.registerCommand(new UpdateId());
    }

    private RegisteredCommands() {
    }

    private static void registerCommand(Command command) {
        MAP.put(command.getName(), command);
    }
}
