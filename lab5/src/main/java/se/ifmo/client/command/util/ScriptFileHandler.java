package se.ifmo.client.command.util;

import se.ifmo.client.console.Console;
import se.ifmo.system.file.handler.IOHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScriptFileHandler implements IOHandler<String> {
    private final BufferedReader bufferedReader;
    private final Console console;
    private final Path scriptPath;

    public ScriptFileHandler(Path scriptPath, Console console) throws FileNotFoundException {
        this.console = console;
        this.scriptPath = scriptPath;
        if (Files.notExists(scriptPath)) {
            System.err.println("File " + scriptPath.getFileName() + " is not found");
            throw new FileNotFoundException();
        }
        bufferedReader = new BufferedReader(new FileReader(scriptPath.toFile()));

    }

    @Override
    public String read() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            System.err.println("Error reading file: " + scriptPath);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void write(String data) {
        console.write(data);
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }
}
