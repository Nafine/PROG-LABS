package se.ifmo.shared.communication;

import java.io.Serializable;
import java.util.List;

/**
 * Request which used for routing requests to commands.
 *
 * @param command name.
 * @param args List of String, command arguments.
* */
public record Request(String command, List<String> args) implements Serializable {
}
