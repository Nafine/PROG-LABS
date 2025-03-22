package se.ifmo.shared.io;

public interface IOHandler<T> extends AutoCloseable {

    /**
     * Read next available piece of data from input channel.
     *
     * @return information retrieved
     */
    T read();

    /**
     * First {@link #write} prompt and call {@link #read}.
     *
     * @param prompt to write
     * @return result of {@link #read}
     */
    default T read(T prompt) {
        write(prompt);
        return read();
    }

    /**
     * Writes data into output channel.
     *
     * @param prompt to write
     */
    void write(T prompt);
}
