package se.ifmo.system.file.handler;

/**
 * Class which represents behavior of all file handlers for the program.
 * @param <T>
 */
public interface IOHandler<T> extends AutoCloseable {
      /**
       * Read all file
       * @return {@link T}
       */
      T read();

      /**
       * Write to file.
       * @param data {@link T}
       */
      void write(T data);
}
