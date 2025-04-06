package se.ifmo.shared.command;

import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.model.Vehicle;
import se.ifmo.shared.exceptions.InvalidDataException;

/**
 * Removes all elements of collection that are greater than specified element.
 */
public class RemoveGreater extends Command {
    /**
     * Constructs a new {@link RemoveGreater} command.
     */
    public RemoveGreater() {
        super("remove_greater", new String[]{"name", "coordinate_x", "coordinate_y", "engine_power", "capacity", "distance_traveled", "fuel_type"}, "Removes all elements that are greater than specified element");
    }

    /**
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        Vehicle vehicle = VehicleDirector.constructAndGetVehicle(req.args());

        CollectionManager.getInstance().getCollection().removeIf(temp -> temp.compareTo(vehicle) < 0);
        return new Callback("Successfully deleted all matching elements");
    }
}
