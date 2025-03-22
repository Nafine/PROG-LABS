package se.ifmo.shared.communication;

import se.ifmo.client.console.Console;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.communication.exceptions.AlreadyRunningScriptException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet; /**
 * Subclass of {@link Handler} and used to handle scripts specific way.
 */
public class ScriptHandler extends Handler implements AutoCloseable {
    private static final HashSet<String> runningScripts = new HashSet<>();
    Path scriptPath;
    BufferedReader bufferedReader;

    /**
     * Constructs a new {@link ScriptHandler} class.
     *
     * @param scriptPath file path to a script
     * @param console    console to output command's {@link Callback}
     * @throws IOException                   if some {@link IOException} occurred during reading script file
     * @throws AlreadyRunningScriptException if script endless recursion detected
     */
    public ScriptHandler(Path scriptPath, Console console) throws IOException, AlreadyRunningScriptException {
        super(console);
        this.scriptPath = scriptPath;

        if (Files.notExists(scriptPath))
            throw new FileNotFoundException("Failed to find " + scriptPath + " script.");

        bufferedReader = new BufferedReader(new FileReader(scriptPath.toFile()));

        if (runningScripts.contains(scriptPath.getFileName().toString()))
            throw new AlreadyRunningScriptException(scriptPath.getFileName().toString());

        runningScripts.add(scriptPath.getFileName().toString());
    }

    /**
     * Handles input the same way as {@link Handler#run()}, but reads lines from file.
     */
    @Override
    public void run() {
        CollectionManager.getInstance();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                handle(line);
            }
        } catch (InterruptedException e) {
            console.writeln("Closing file: " + e.getMessage());
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
