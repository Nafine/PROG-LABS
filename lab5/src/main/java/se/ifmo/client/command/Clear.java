package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;

public class Clear extends Command {
    public Clear() {
        super("clear", "Clear the collection");
    }

    @Override
    public Callback execute(Request req) {
        CollectionManager.getInstance().getCollection().clear();
        return new Callback("Collection successfully cleared");
    }
}
