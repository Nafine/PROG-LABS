package se.ifmo.client.command;

import se.ifmo.client.builders.VehicleDirector;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.LinkedHashSet;

/**
 * Reads and updates element by specified id.
 */
public class UpdateId extends Command {

    /**
     * Constructs a new {@link RemoveById} command.
     */
    public UpdateId() {
        super("update_id", new String[]{"name", "coordinate_x", "coordinate_y", "engine_power", "capacity", "distance_traveled", "fuel_type"}, "Update the value of the collection item whose id is equal to the given one");
    }

    /**
     * Reads element, updates its id and replaces existing element with a new one.
     * <p>
     * Asks user to enter exactly one {@link se.ifmo.system.collection.model.Vehicle} using {@link VehicleDirector}.
     * Removes element which have specified id.
     * Adds new element.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        int id = Integer.parseInt(req.args().get(0));

        Vehicle vehicle = VehicleDirector.constructAndGetVehicle(req.args());
        vehicle.setId(id);

        LinkedHashSet<Vehicle> collection = CollectionManager.getInstance().getCollection();
        collection.remove(collection.stream().filter(temp -> temp.getId() == id).findFirst().get());
        collection.add(vehicle);

        return new Callback("Successfully updated an element by id " + req.args().get(0));
    }
}
