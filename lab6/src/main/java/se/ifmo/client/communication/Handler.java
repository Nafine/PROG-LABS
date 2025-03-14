package se.ifmo.client.communication;

import se.ifmo.client.communication.exceptions.AlreadyRunningScriptException;
import se.ifmo.client.communication.exceptions.ProgramFinishedException;
import se.ifmo.client.console.Console;
import se.ifmo.system.collection.CollectionManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Class, which handles user's input and transfers it to the {@link Router} class.
 */
public class Handler implements Runnable {
    protected final Console console;
    protected final Router router;

    /**
     * Constructs a new {@link Handler} class.
     *
     * @param console to read from
     */
    public Handler(Console console) {
        this.console = console;
        this.router = Router.getInstance();
    }

    /**
     * Method which handles exactly one prompt and calls {@link Router#route(Request)} method.
     *
     * @param prompt user's prompt
     * @throws InterruptedException     if something went wrong
     * @throws ProgramFinishedException if program needs to be finished
     */
    protected void handle(String prompt) throws InterruptedException {
        if (prompt == null) return;
        Callback callback = router.route(parse(prompt));

        if (callback.message() != null && callback.message().equals("exit")) throw new ProgramFinishedException();
        if (callback.message() != null && callback.message().startsWith("script")) {
            try (ScriptHandler scriptHandler = new ScriptHandler(Path.of(callback.message().substring("script".length())), console)) {
                scriptHandler.run();
            } catch (AlreadyRunningScriptException e) {
                console.writeln("Script " + e.getMessage() + " already running.");
            } catch (IOException e) {
                console.writeln("IO error occurred: " + e.getMessage());
            }
            finally {
                return;
            }
        }
        if (callback.message() != null && !callback.message().isBlank()) console.writeln(callback.message());
        if (callback.vehicles() != null && !callback.vehicles().isEmpty())
            callback.vehicles().forEach(vehicle -> console.writeln(vehicle.toString()));
    }

    /**
     * Parse exactly one prompt.
     *
     * @param prompt user's prompt
     * @return {@link Request}
     */
    protected Request parse(String prompt) {
        final String[] parts = prompt.split("\\s+", 2);

        final String command = parts[0];
        final List<String> args = parts.length > 1 ? Arrays.asList(parts[1].split("\\s+")) : Collections.emptyList();

        return new Request(command, args);
    }

    /**
     * Begin to read user's input and {@link #handle(String)} each line.
     * <p>
     * Cyclically reads lines from input and handles each line.
     * Catches exception from lower level.
     * </p>
     */
    @Override
    public void run() {
        console.writeln("Daite 100 ballov");
        //load collection
        CollectionManager.getInstance();
        try {
            String line;
            while ((line = console.read("")) != null) {
                handle(line);
            }
        } catch (InterruptedException e) {
            console.writeln("Closing program.");
        } catch (ProgramFinishedException e) {
            throw e;
        } catch (Exception e) {
            console.writeln("Some error occurred:" + e.getMessage());
        }
    }
}

/**
 * Subclass of {@link Handler} and used to handle scripts specific way.
 */
class ScriptHandler extends Handler implements AutoCloseable {
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

