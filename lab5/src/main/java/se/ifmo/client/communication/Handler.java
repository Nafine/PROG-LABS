package se.ifmo.client.communication;

import se.ifmo.client.console.Console;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.collection.util.VehicleReader;

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

    private void handle(String prompt) {
        if (prompt == null) return;
        Callback callback = router.route(parse(prompt));
    }

    private void handleCallback(Callback callback) {
        if (callback.message() != null && !callback.message().isBlank()) console.write(callback.message());
        if (callback.vehicles() != null && !callback.vehicles().isEmpty()) callback.vehicles().forEach(vehicle -> {
            console.write(vehicle.toString());
        });
    }

    private Request parse(String prompt) {
        final String[] parts = prompt.split(" ", 2);

        final String command = parts[0];
        final List<String> args = parts.length > 1 ? Arrays.asList(parts[1].split(" ")) : Collections.emptyList();
        final List<Vehicle> vehicles = new LinkedList<>();

        int elementsRequired = router.getElementsRequiredFor(command);

        while (elementsRequired-- > 0) {
            try {
                vehicles.add(VehicleReader.readElement(console));
            } catch (InterruptedException e) {
                console.write("Command has been interrupted");
                return null;
            }
        }

        return new Request(command, args, vehicles);
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
        } catch (Exception e) {
            console.writeln("Some error occured:" + e.getMessage());
        }
    }
}
