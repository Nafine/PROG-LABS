package se.ifmo.shared.communication;

import se.ifmo.shared.model.Vehicle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Record which represents callback from a command.
 *
 * @param message  command output
 * @param vehicles List of {@link Vehicle} if command interacts with collection.
 */
public record Callback(String message, List<Vehicle> vehicles) implements Serializable {

    /**
     * Constructs a new {@link Callback} record.
     *
     * @param message  command output
     * @param vehicles vehicles that need to be handled
     */
    public Callback(String message, Vehicle... vehicles) {
        this(message, List.of(vehicles));
    }

    /**
     * Constructs a new {@link Callback} record.
     *
     * @param message command output
     */
    public Callback(String message) {
        this(message, Collections.emptyList());
    }

    /**
     * Constructs an empty {@link Callback} record.
     * <p>
     * Calls 1-argument constructor with null argument.
     * </p>
     *
     * @return {@link Callback}
     */
    public static Callback empty() {
        return new Callback(null);
    }
}
