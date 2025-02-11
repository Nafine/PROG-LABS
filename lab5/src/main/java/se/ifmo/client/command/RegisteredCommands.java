package se.ifmo.client.command;

import java.util.List;

public class RegisteredCommands {
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
