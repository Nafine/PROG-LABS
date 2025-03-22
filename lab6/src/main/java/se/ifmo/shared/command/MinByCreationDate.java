package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.model.Vehicle;

import java.util.Comparator;

/**
 * Outputs any element of collection with the earliest time of creation.
 */
public class MinByCreationDate extends Command {
    /**
     * Constructs a new {@link MinByCreationDate} command.
     */
    public MinByCreationDate() {
        super("min_by_creation_date", "Output any object from the collection whose creationDate field value is minimal");
    }

    /**
     * Filters all elements of collection comparing their creation date.
     * <p>
     * Gets collection and filters it using comparing by creation date.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        var minVehicle = CollectionManager.getInstance().getCollection().stream().min(Comparator.comparing(Vehicle::getCreationDate));

        return minVehicle.map(vehicle -> new Callback(vehicle.toString())).orElseGet(() -> new Callback("Collection is empty"));
    }
}
