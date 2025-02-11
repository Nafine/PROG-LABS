package se.ifmo.client.communication;

import se.ifmo.client.console.Console;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Handler implements Runnable {
    private final Console console;
    private final Router router;

    public Handler(Console console) {
        this.console = console;
        this.router = Router.getInstance();
    }

    private void handle(String prompt) throws InterruptedException {
        if (prompt == null) return;
        Callback callback = router.route(parse(prompt));

        if (callback.message() != null && callback.message().equals("exit")) throw new InterruptedException();

        if (callback.message() != null && !callback.message().isBlank()) console.write(callback.message());
        if (callback.vehicles() != null && !callback.vehicles().isEmpty()) callback.vehicles().forEach(vehicle -> {
            console.writeln(vehicle.toString());
        });
    }

    private Request parse(String prompt) {
        final String[] parts = prompt.split(" ", 2);

        final String command = parts[0];
        final List<String> args = parts.length > 1 ? Arrays.asList(parts[1].split(" ")) : Collections.emptyList();
        final List<Vehicle> vehicles = new LinkedList<>();

        return new Request(command, args, vehicles, console);
    }

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
        } catch (Exception e) {
            console.writeln("Some error occurred:" + e.getMessage());
        }
    }
}
