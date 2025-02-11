package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;

public class RemoveById extends Command {
    public RemoveById() {
        super("remove_by_id", new String[]{"id"}, "Remove element by its id");
    }

    @Override
    public Callback execute(Request req) {
        try {
            CollectionManager.getInstance().getCollection().removeIf(vehicle -> vehicle.getId() == Integer.parseInt(req.args().get(0)));
            return new Callback("Successfully deleted element by id " + req.args().get(0));
        } catch (IndexOutOfBoundsException e) {
            return new Callback("Command arguments cannot be empty");
        }
    }
}
