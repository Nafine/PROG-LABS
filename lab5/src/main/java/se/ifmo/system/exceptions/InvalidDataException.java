package se.ifmo.system.exceptions;

import se.ifmo.client.command.Add;

/**
 * Checked exception which represents that invalid data was handled.
 */
public class InvalidDataException extends Exception {
    private final Object obj;

    /**
     * Constructs a new {@link InvalidDataException} exception.
     * @param obj failed
     * @param message of what happened
     */
    public InvalidDataException(Object obj, String message) {
        super(message);
        this.obj = obj;
    }

    /**
     * Calls {@link Exception#getMessage()}.
     * @return {@link String}
     */
    @Override
    public String getMessage() {
        return obj.getClass() + ":" + super.getMessage();
    }
}
