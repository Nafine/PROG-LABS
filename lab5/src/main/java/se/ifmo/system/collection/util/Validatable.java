package se.ifmo.system.collection.util;

import se.ifmo.system.exceptions.InvalidDataException;

public interface Validatable {
    void validate() throws InvalidDataException;
}
