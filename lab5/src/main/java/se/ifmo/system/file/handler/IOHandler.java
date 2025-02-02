package se.ifmo.system.file.handler;

import java.io.IOException;

public interface IOHandler<T> extends AutoCloseable {
      T read() throws IOException;

      void write(T data) throws IOException;
}
