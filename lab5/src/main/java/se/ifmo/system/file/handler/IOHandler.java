package se.ifmo.system.file.handler;

public interface IOHandler<T> extends AutoCloseable {
      T read();

      void write(T data);
}
