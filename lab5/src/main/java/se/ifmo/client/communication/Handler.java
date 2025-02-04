package se.ifmo.client.communication;

import se.ifmo.client.console.Console;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Handler implements Runnable{
    private final Console console;

    public Handler(Console console){
        this.console = console;
    }

    private void handle(String prompt){
        if (prompt == null) return;
        Router.getInstance().route(parse(prompt));
    }

    private Request parse(String prompt){
        final String[] parts = prompt.split(" ", 2);

        final String command = parts[0];
        final List<String> args = parts.length > 1 ? Arrays.asList(parts[1].split(" ")) : Collections.emptyList();
        final List<Vehicle> persons = new LinkedList<>();

        int elementsRequired = Router.getInstance().getElementsRequiredFor(command);

        return new Request("", Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public void run() {
        console.write("Dai 100 ballov");
        CollectionManager.getInstance();
        try{
            String line;
            while((line = console.read()) != null) handle(line);
        }
        catch(Exception e){
            console.write("Some error occured:\n" + e.getMessage());
        }
    }
}
