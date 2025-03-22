package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;

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
