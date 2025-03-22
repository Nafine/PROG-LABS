package se.ifmo.shared.command;

import se.ifmo.client.builders.VehicleDirector;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.model.Vehicle;
import se.ifmo.shared.exceptions.InvalidDataException;

import java.util.List;

/**
 * Creates n random {@link Vehicle} elements.
 */
public class AddRandom extends Command {
    /**
     * Constructs a new {@link RemoveById} command.
     */
    public AddRandom() {
        super("add_random", new String[]{"amount"}, "Adds n random elements to the collection");
    }

    /**
     * Creates n random {@link Vehicle} elements.
     * Calls {@link VehicleDirector#constructAndGetRandomVehicles(int)}.
     * @param req {@link Request}
     * @return
     * @throws InvalidDataException
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        List<Vehicle> carFleet = VehicleDirector.constructAndGetRandomVehicles(Integer.parseInt(req.args().get(0)));
        CollectionManager.getInstance().getCollection().addAll(carFleet);
        return new Callback("Successfully added new " + req.args().get(0) + " elements to the collection");
    }
}
