package se.ifmo.system.collection.util;

import se.ifmo.system.exceptions.InvalidDataException;

/**
 * Interface which defines how you should validate your classes.
 */
public interface Validatable {
    void validate() throws InvalidDataException;
}
