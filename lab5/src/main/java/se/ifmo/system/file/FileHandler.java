package se.ifmo.system.file;

import lombok.Getter;
import se.ifmo.system.file.handler.IOHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements IOHandler<String> {
    protected final Path filePath;

    @Getter
    protected final BufferedInputStream bufferedInputStream;
    @Getter
    protected final BufferedWriter bufferedWriter;

    public FileHandler(Path filePath, boolean append) throws IOException {
        this.filePath = filePath;

        if (Files.notExists(filePath)) {
            System.err.println("File " + filePath.getFileName() + "is not found");
            throw new FileNotFoundException();
        }

        bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath.toFile()));
        bufferedWriter = new BufferedWriter(new FileWriter(filePath.toFile(), append));
    }

    public FileHandler(Path filePath) throws IOException {
        this.filePath = filePath;

        if (Files.notExists(filePath)) {
            System.err.println("File " + filePath.getFileName() + "is not found");
            throw new FileNotFoundException();
        }

        bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath.toFile()));
        bufferedWriter = new BufferedWriter(new FileWriter(filePath.toFile(), true));
    }

    @Override
    public String read() {
        StringBuilder content = new StringBuilder();
        int nextChar;
        try {
            while ((nextChar = bufferedInputStream.read()) != -1) {
                content.append((char) nextChar);
            }
            return String.valueOf(content);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void write(String data) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath.getFileName());
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        bufferedInputStream.close();
        bufferedWriter.close();
    }
}
