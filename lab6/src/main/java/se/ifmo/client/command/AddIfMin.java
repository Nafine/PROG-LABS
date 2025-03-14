package se.ifmo.client.command;

import se.ifmo.client.builders.VehicleDirector;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

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
