package se.ifmo.client.communication;

import se.ifmo.client.communication.exceptions.ProgramFinishedException;
import se.ifmo.client.console.Console;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
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

        if (callback.message() != null && !callback.message().isBlank()) console.writeln(callback.message());
        if (callback.vehicles() != null && !callback.vehicles().isEmpty()) callback.vehicles().forEach(vehicle -> {
            console.writeln(vehicle.toString());
        });
    }

    /**
     * Parse exactly one prompt.
     *
     * @param prompt user's prompt
     * @return {@link Request}
     */
    protected Request parse(String prompt) {
        final String[] parts = prompt.split(" ", 2);

        final String command = parts[0];
        final List<String> args = parts.length > 1 ? Arrays.asList(parts[1].split(" ")) : Collections.emptyList();
        final List<Vehicle> vehicles = new LinkedList<>();

        return new Request(command, args, vehicles, console);
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
