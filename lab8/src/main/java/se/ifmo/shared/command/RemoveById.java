package se.ifmo.shared.command;

import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;

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
     * Removes element by id from collection.
     *
     * <p>
     * Deletion will only be performed when deletion from database was successful.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        if (CollectionManager.getInstance().removeById(Integer.parseInt(req.args().get(0))))
            return new Callback("Successfully deleted element by id " + req.args().get(0));
        return new Callback("Cannot delete element by id " + req.args().get(0));
    }
}
