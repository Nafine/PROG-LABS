package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;

/**
 * Removes element by its id.
 */
public class RemoveById extends Command {
    /**
     * Constructs a new {@link RemoveById} command.
     */
    public RemoveById() {
        super("remove_by_id", new String[]{"id"}, "Remove element by its id");
    }

    /**
     * Calls removeIf() on collection comparing element's id.
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        CollectionManager.getInstance().getCollection().removeIf(vehicle -> vehicle.getId() == Integer.parseInt(req.args().get(0)));
        return new Callback("Successfully deleted element by id " + req.args().get(0));
    }
}
