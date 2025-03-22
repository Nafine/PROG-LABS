package se.ifmo.server.file;

import se.ifmo.shared.io.IOHandler;

import java.util.LinkedHashSet;

public interface CollectionParser<T> extends IOHandler<LinkedHashSet<T>> {
}
