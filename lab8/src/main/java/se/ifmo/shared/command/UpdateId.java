package se.ifmo.shared.command;

import se.ifmo.server.collection.CollectionManager;
import se.ifmo.server.db.UserService;
import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.exceptions.InvalidDataException;
import se.ifmo.shared.model.Vehicle;

import java.util.LinkedHashSet;

/**
 * Reads and updates element by specified id.
 */
public class UpdateId extends Command {

    /**
     * Constructs a new {@link RemoveById} command.
     */
    public UpdateId() {
        super("update_id", new String[]{"id", "name", "coordinate_x", "coordinate_y", "engine_power", "capacity", "distance_traveled", "fuel_type"}, "Update the value of the collection item whose id is equal to the given one");
    }

    /**
     * Reads element, updates its id and replaces existing element with a new one.
     * <p>
     * Asks user to enter exactly one {@link Vehicle} using {@link VehicleDirector}.
     * Removes element which have specified id.
     * Adds new element.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        int id = Integer.parseInt(req.args().getFirst());
        long uid = UserService.getInstance().getUserID(req.credentials().username());

        Vehicle vehicle = VehicleDirector.constructAndGetVehicle(req.args().subList(1, req.args().size()), uid);
        vehicle.setId(id);

        LinkedHashSet<Vehicle> collection = CollectionManager.getInstance().getCollection();
        collection.remove(collection.stream().filter(temp -> temp.getId() == id).findFirst().get());
        collection.add(vehicle);

        return new Callback("Successfully updated an element by id " + req.args().get(0));
    }
}
