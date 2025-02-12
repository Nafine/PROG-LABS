package se.ifmo.client.communication;

import se.ifmo.client.communication.exceptions.AlreadyRunningScriptException;
import se.ifmo.client.console.Console;
import se.ifmo.system.collection.CollectionManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class ScriptHandler extends Handler implements AutoCloseable {
    private static HashSet<String> runningScripts = new HashSet<>();
    Path scriptPath;
    BufferedReader bufferedReader;

    public ScriptHandler(Path scriptPath, Console console) throws IOException, AlreadyRunningScriptException {
        super(console);
        this.scriptPath = scriptPath;

        if (Files.notExists(scriptPath)) {
            System.err.println("File " + scriptPath.getFileName() + "is not found");
            throw new FileNotFoundException();
        }
        bufferedReader = new BufferedReader(new FileReader(scriptPath.toFile()));

        if (runningScripts.contains(scriptPath.getFileName().toString()))
            throw new AlreadyRunningScriptException(scriptPath.getFileName().toString());

        runningScripts.add(scriptPath.getFileName().toString());
    }

    @Override
    public void run() {
        CollectionManager.getInstance();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                handle(line);
            }
        } catch (InterruptedException e) {
            console.writeln("Closing file");
        } catch (Exception e) {
            console.writeln("Some error occurred:" + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
        runningScripts.remove(scriptPath.getFileName().toString());
    }
}
