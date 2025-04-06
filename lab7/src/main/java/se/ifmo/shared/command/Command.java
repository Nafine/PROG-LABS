package se.ifmo.shared.command;

import lombok.Getter;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.exceptions.InvalidDataException;


/**
 * Abstract base class representing a command.
 * <p>
 * Specifies a single execute method for command execution, which receives {@link Request} and returns {@link Callback}.
 * </p>
 *
 * @see Request
 * @see Callback
 */
@Getter
public abstract class Command {
    public static final String[] EMPTY_ARGS = new String[0];

    private final String name;
    private final String[] args;
    private final String description;

    /**
     * Construct commands with specified arguments.
     * @param name command name
     * @param description what this command does
     */
    protected Command(String name, String description) {
        this.name = name;
        this.args = EMPTY_ARGS;
        this.description = description;
    }

    /**
     * Construct commands with specified arguments.
     * @param name command name
     * @param args command arguments
     * @param description what this command does
     */
    protected Command(String name, String[] args, String description) {
        this.name = name;
        this.args = args;
        this.description = description;
    }

    /**
     * An abstract method, defines what command must do
     * @param request {@link Request}
     * @return callback
     */
    public abstract Callback execute(Request request) throws InvalidDataException;
}
