package se.ifmo.shared.communication;

import se.ifmo.shared.command.Command;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Request which used for routing requests to commands.
 *
 * @param command name.
 * @param args    List of String, command arguments.
 */
public record Request(String command, List<String> args, Credentials credentials) implements Serializable {
    @Serial
    private static final long serialVersionUID = 5874551785209763956L;
    public Request(Command command, List<String> args, Credentials credentials) {
        this(command.getName(), args, credentials);
    }
}
