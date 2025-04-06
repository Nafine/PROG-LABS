package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;

public class Save extends Command {
    /**
     * Constructs a new {@link Save} command.
     */
    public Save() {
        super("save", "Saves the collection to file");
    }

    /**
     * Writes current collection state to a file that is specified by an environment variable.
     * <p>
     * Calls save method on {@link CollectionManager}.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        CollectionManager.getInstance().save();

        return new Callback("Successfully saved the collection to file");
    }
}
