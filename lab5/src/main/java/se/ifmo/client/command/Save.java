package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;

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
