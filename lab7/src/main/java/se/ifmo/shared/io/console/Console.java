package se.ifmo.shared.io.console;

import se.ifmo.shared.io.IOHandler;

/**
 * An interface which defines how console should work.
 */
public interface Console extends IOHandler<String> {

    /**
     * Calls {@link IOHandler#write} concatenated with {@link System#lineSeparator()}.
     *
     * @param line
     */
    default void writeln(String line) {
        write(line + System.lineSeparator());
    }

    /**
     * Method which used for formatted output.
     * <p>
     * Works exactly like {@link String#format}.
     * </p>
     *
     * @param format string
     * @param args   to insert
     */
    default void writef(String format, Object... args) {
        write(String.format(format, args));
    }

    /**
     * @return true if ready to read
     */
    boolean ready();
}
