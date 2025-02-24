package se.ifmo.client.communication;

import se.ifmo.client.command.util.ScriptFileHandler;
import se.ifmo.client.communication.exceptions.AlreadyRunningScriptException;
import se.ifmo.client.console.Console;
import se.ifmo.system.collection.CollectionManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;

/**
 * Subclass of {@link Handler} and used to handle scripts specific way.
 */
public class ScriptHandler extends Handler implements AutoCloseable {
    private static final HashSet<String> runningScripts = new HashSet<>();
    private final ScriptFileHandler scriptFileHandler;
    Path scriptPath;


    /**
     * Constructs a new {@link ScriptHandler} class.
     *
     * @param scriptPath file path to a script
     * @param console    io to output command's {@link Callback}
     * @throws IOException                   if some {@link IOException} occurred during reading script file
     * @throws AlreadyRunningScriptException if script endless recursion detected
     */
    public ScriptHandler(Path scriptPath, Console console) throws IOException, AlreadyRunningScriptException {
        super(new ScriptFileHandler(scriptPath, console));
        this.scriptPath = scriptPath;
        this.scriptFileHandler = new ScriptFileHandler(scriptPath, console);

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
            while ((line = scriptFileHandler.read()) != null) {
                handle(line);
            }
        } catch (InterruptedException e) {
            io.write("Closing file");
        } catch (Exception e) {
            io.write("Some error occurred:" + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        scriptFileHandler.close();
        runningScripts.remove(scriptPath.getFileName().toString());
    }
}
