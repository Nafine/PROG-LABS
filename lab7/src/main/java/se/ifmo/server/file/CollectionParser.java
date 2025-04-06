package se.ifmo.server.file;

import se.ifmo.shared.io.IOHandler;

import java.util.LinkedHashSet;

/**
 * Transitional interface for file parser classes.
 * @param <T>
 */
public interface CollectionParser<T> extends IOHandler<LinkedHashSet<T>> {
}
