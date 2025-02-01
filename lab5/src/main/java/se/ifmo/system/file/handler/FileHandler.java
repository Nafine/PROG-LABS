package se.ifmo.system.file.handler;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public abstract class FileHandler<T> {
    protected final Path handlingFilePath;


    public FileHandler(Path handlingFilePath) {
        this.handlingFilePath = handlingFilePath;
    }

    public final FileReader getReader() throws IOException {
        return new FileReader(handlingFilePath.toFile());
    }

    public final FileOutputStream getOutputStream() throws IOException {
        return new FileOutputStream(handlingFilePath.toFile());
    }

    public FileOutputStream getOutputStream(boolean append) throws IOException {
        return new FileOutputStream(handlingFilePath.toFile(), append);
    }

    abstract public T read();

    abstract public void write(T value);
}
