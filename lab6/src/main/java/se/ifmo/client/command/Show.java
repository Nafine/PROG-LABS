package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;

/**
 * Shows elements of whole collction.
 */
public class Show extends Command {
    /**
     * Constructs a new {@link RemoveById} command.
     */
    public Show() {
        super("show", "Shows whole collection");
    }

    /**
     * Gets collection using {@link CollectionManager}, casts it to list and returns it.
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        return new Callback("Collection:", CollectionManager.getInstance().getCollection().stream().toList());
    }
}
