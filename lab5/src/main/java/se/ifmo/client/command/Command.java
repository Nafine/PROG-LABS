package se.ifmo.client.command;

import lombok.Getter;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

@Getter
public abstract class Command {
    public static final String[] EMPTY_ARGS = new String[0];

    private final String name;
    private final String[] args;
    private final String description;

    protected Command() {
        this.name = "";
        this.args = EMPTY_ARGS;
        this.description = "";
    }

    protected Command(String name, String description) {
        this.name = name;
        this.args = EMPTY_ARGS;
        this.description = description;
    }

    protected Command(String name, String[] args, String description) {
        this.name = name;
        this.args = args;
        this.description = description;
    }

    public abstract Callback execute(Request request);
}
