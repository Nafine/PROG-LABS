package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class AddRandom extends Command {
    public AddRandom() {
        super("add_random", new String[]{"count"}, "Adds n random elements to the collection");
    }

    @Override
    public Callback execute(Request req){
        return Callback.empty();
    }
}
