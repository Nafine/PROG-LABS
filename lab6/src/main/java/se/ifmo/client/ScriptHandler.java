package se.ifmo.client;

import se.ifmo.server.file.FileHandler;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.communication.Router;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class ScriptHandler {
    private final static HashSet<String> runningScripts = new HashSet<>();
    private final Client client;

    public ScriptHandler(Client client) {
        this.client = client;
    }

    public Callback handleScript(Request request) {
        File file;
        try {
            file = Path.of(request.args().get(0)).toFile();
        } catch (InvalidPathException e) {
            return new Callback("String is not a path.");
        }

        if (!file.exists()) return new Callback("File not found.");
        if (!file.isFile()) return new Callback("Path is not a file.");
        if (!file.canRead()) return new Callback("Not enough rights to read file.");

        if (runningScripts.contains(file.getAbsolutePath())) return new Callback("Script already running.");
        runningScripts.add(file.getAbsolutePath());

        try (FileHandler fileHandler = new FileHandler(file.toPath())) {
            Queue<String> scriptQueue = new LinkedList<>();
            String token;
            while (!(token = fileHandler.read()).isEmpty())
                Collections.addAll(scriptQueue, token.split(System.lineSeparator() + "+"));
            while (!scriptQueue.isEmpty()) client.handle(scriptQueue.poll());

            runningScripts.remove(file.getAbsolutePath());

            return Router.getInstance().route(request);
        } catch (IOException e) {
            runningScripts.remove(file.getAbsolutePath());
            System.err.println("Error closing file: " + e.getMessage());
            return new Callback("Can't close file");
        }
    }
}
