package se.ifmo.client.command;

import java.util.List;

public class Active {
    public static final List<Command> LIST = List.of(
            new Add(),
            new AddIfMax(),
            new AddIfMin(),
            new Clear(),
            new ExecuteScript(),
            new Exit(),
            new FilterGreaterThanManufacturer(),
            new FilterLessThanPartNumber(),
            new Help(),
            new Info(),
            new RemoveAnyByPrice(),
            new RemoveById(),
            new RemoveGreater(),
            new Save(),
            new Show(),
            new UpdateId()
    );

    private Active() {
    }
}
