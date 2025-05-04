package se.ifmo.shared.command;

import se.ifmo.server.collection.CollectionManager;
import se.ifmo.server.db.UserService;
import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.exceptions.InvalidDataException;
import se.ifmo.shared.model.Vehicle;

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
        long uid = UserService.getInstance().getUserID(req.credentials().username());
        Vehicle givenVehicle = VehicleDirector.constructAndGetVehicle(req.args(), uid);

        for (Vehicle collectionVehicle : CollectionManager.getInstance().getCollection())
            if (collectionVehicle.compareTo(givenVehicle) == 1)
                CollectionManager.getInstance().removeById(collectionVehicle.getId());

        return new Callback("Successfully deleted all matching elements");
    }
}
