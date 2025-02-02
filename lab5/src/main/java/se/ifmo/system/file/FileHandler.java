package se.ifmo.system.file;

import se.ifmo.system.file.handler.IOHandler;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class FileHandler implements IOHandler<String> {
    protected final Path handlingFilePath;

    protected final FileReader fileReader;

    public FileHandler(Path handlingFilePath) throws IOException {
        this.handlingFilePath = handlingFilePath;

        fileReader = new FileReader(handlingFilePath.toFile());
    }

    @Override
    public String read() {
        StringBuilder content = new StringBuilder();
        int nextChar;
        try {
            while ((nextChar = fileReader.read()) != -1) {
                content.append((char) nextChar);
            }
            return String.valueOf(content);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void write(String data) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(handlingFilePath.toFile())) {
            fileOutputStream.write(data.getBytes());
        }
        catch (IOException e) {
            System.err.println("Error writing to file: " + handlingFilePath.toAbsolutePath());
            System.err.println(e.getMessage());
        }
    }

    public void write(String data, boolean append){
        try(FileOutputStream fileOutputStream = new FileOutputStream(handlingFilePath.toFile(), append)) {
            fileOutputStream.write(data.getBytes());
        }
        catch (IOException e) {
            System.err.println("Error writing to file: " + handlingFilePath.toAbsolutePath());
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        fileReader.close();
    }
}
