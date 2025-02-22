package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;

/**
 * Clears whole collection.
 */
public class Clear extends Command {
    /**
     * Constructs a new {@link Clear} command.
     */
    public Clear() {
        super("clear", "Clear the collection");
    }

    /**
     * Call clear() method on collection.
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        CollectionManager.getInstance().getCollection().clear();
        return new Callback("Collection successfully cleared");
    }
}
