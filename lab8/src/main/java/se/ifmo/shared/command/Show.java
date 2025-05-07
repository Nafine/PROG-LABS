package se.ifmo.shared.command;

import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.model.Vehicle;

import java.util.List;

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
        List<Vehicle> list = CollectionManager.getInstance().getCollection().stream().toList();

        return new Callback("Collection:", list);
    }
}
