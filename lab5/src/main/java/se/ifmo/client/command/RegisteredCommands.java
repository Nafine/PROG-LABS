package se.ifmo.client.command;

import java.util.List;

/**
 * Class which contains all registered commands of the program.
 * <p>
 * All registered command can be used in io by user, they handle all type of exceptions that
 * can occur during their execution time.
 * </p>
 */
public class RegisteredCommands {
    /**
     * Public static final field with list of all available commands.
     */
    public static final List<Command> LIST = List.of(
            new Add(),
            new AddIfMin(),
            new Clear(),
            new ExecuteScript(),
            new Exit(),
            new FilterLessThanFuelType(),
            new Help(),
            new History(),
            new Info(),
            new MinByCreationDate(),
            new RemoveById(),
            new RemoveGreater(),
            new Save(),
            new Show(),
            new SumOfEnginePower(),
            new UpdateId()
    );

    private RegisteredCommands() {
    }
}
