package se.ifmo.shared.command;

import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.model.Vehicle;
import se.ifmo.shared.exceptions.InvalidDataException;

import java.util.Comparator;

/**
 * Reads element using {@link VehicleDirector} and adds it to the collection if it's lesser than all elements of collection.
 */
public class AddIfMin extends Command {
    /**
     * Constructs a new {@link AddIfMin} command.
     */
    public AddIfMin() {
        super("add_if_min", "Add a new element to a collection if its value is less than the smallest element of this collection");
    }

    /**
     * Executes the command to add an element.
     * <p>
     * Asks user to enter exactly one {@link Vehicle}
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        Vehicle vehicle = VehicleDirector.constructAndGetVehicle(req.args());

        var minVehicle = CollectionManager.getInstance().getCollection().stream().min(Comparator.naturalOrder());
        if (minVehicle.isPresent() && vehicle.compareTo(minVehicle.get()) < 0) {
            CollectionManager.getInstance().getCollection().add(minVehicle.get());
            return new Callback("Successfully added new element to the collection");
        }
        return new Callback("Failed to add new element to the collection, it's not the smallest element");
    }
}
