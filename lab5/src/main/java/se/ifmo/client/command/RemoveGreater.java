package se.ifmo.client.command;

import se.ifmo.client.builders.VehicleDirector;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

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
    public Callback execute(Request req) throws InvalidDataException, InterruptedException {
        Vehicle vehicle = VehicleDirector.constructAndGetVehicle(req.console());

        CollectionManager.getInstance().getCollection().removeIf(temp -> temp.compareTo(vehicle) < 0);
        return new Callback("Successfully deleted all matching elements");
    }
}
