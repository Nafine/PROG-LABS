package se.ifmo.server.collection.util;

import se.ifmo.shared.exceptions.InvalidDataException;

/**
 * Interface which defines how you should validate your classes.
 */
public interface Validatable {
    void validate() throws InvalidDataException;
}
