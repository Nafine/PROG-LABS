package se.ifmo.shared.command;

import se.ifmo.server.collection.CollectionManager;
import se.ifmo.server.db.UserService;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;

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
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        long uid = UserService.getInstance().getUserID(req.credentials().username());
        CollectionManager.getInstance().clearByUser(uid);
        CollectionManager.getInstance().getCollection().removeIf(vehicle -> vehicle.getOwnerId() == uid);
        return new Callback("Collection successfully cleared");
    }
}
