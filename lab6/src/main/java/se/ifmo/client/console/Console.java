package se.ifmo.client.console;

import se.ifmo.shared.io.IOHandler;

/**
 * An interface which defines how console should work.
 */
public interface Console extends IOHandler<String> {

    default void writeln(String line) {
        write(line + System.lineSeparator());
    }

    default void writef(String format, Object... args) {
        write(String.format(format, args));
    }
}
