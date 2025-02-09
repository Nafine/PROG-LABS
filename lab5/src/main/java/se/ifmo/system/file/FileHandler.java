package se.ifmo.system.file;

import se.ifmo.system.file.handler.IOHandler;

import java.io.*;
import java.nio.file.Path;

public class FileHandler implements IOHandler<String> {
    protected final Path handlingFilePath;

    protected final BufferedInputStream inputStream;
    protected final BufferedWriter bufferedWriter;

    public FileHandler(Path handlingFilePath, boolean append) throws IOException {
        this.handlingFilePath = handlingFilePath;

        inputStream = new BufferedInputStream(new FileInputStream(handlingFilePath.toFile()));
        bufferedWriter = new BufferedWriter(new FileWriter(handlingFilePath.toFile(), append));
    }

    public FileHandler(Path handlingFilePath) throws IOException {
        this.handlingFilePath = handlingFilePath;

        inputStream = new BufferedInputStream(new FileInputStream(handlingFilePath.toFile()));
        bufferedWriter = new BufferedWriter(new FileWriter(handlingFilePath.toFile()));
    }

    @Override
    public String read() {
        StringBuilder content = new StringBuilder();
        int nextChar;
        try {
            while ((nextChar = inputStream.read()) != -1) {
                content.append((char) nextChar);
            }
            return String.valueOf(content);
        } catch (IOException e) {
            System.err.println("Error reading file: " + handlingFilePath);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void write(String data) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(handlingFilePath.toFile())) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + handlingFilePath.getFileName());
            System.err.println(e.getMessage());
        }
    }

    public void write(String data, boolean append) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(handlingFilePath.toFile(), append)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + handlingFilePath.getFileName());
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        bufferedWriter.close();
    }
}
