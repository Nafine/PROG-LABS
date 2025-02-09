package se.ifmo.client.command;

import lombok.Getter;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

@Getter
public abstract class Command {
    private final String name;
    private final String[] args;
    private final String help;
    private final int elementsRequired;

    protected Command(
            final String name,
            final String[] args,
            final String help,
            final int elementsRequired
    ) {
        this.name = name;
        this.args = args;
        this.help = help;
        this.elementsRequired = elementsRequired;
    }

    public abstract Callback execute(Request request);
}
