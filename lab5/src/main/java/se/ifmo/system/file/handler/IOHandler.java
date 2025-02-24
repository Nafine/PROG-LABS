package se.ifmo.system.file.handler;

/**
 * Class which represents behavior of all file handlers for the program.
 *
 * @param <T>
 */
public interface IOHandler<T> extends AutoCloseable {
    /**
     * Read all file
     *
     * @return {@link T}
     */
    T read();

    /**
     * Calls {@link IOHandler#write(Object)} and then {@link IOHandler#read()}
     * @param data to prompt
     * @return {@link T}
     */
    default T read(T data) {
        write(data);
        return read();
    }

    /**
     * Write to file.
     *
     * @param data {@link T}
     */
    void write(T data);
}
