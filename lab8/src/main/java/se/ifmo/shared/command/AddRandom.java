package se.ifmo.shared.command;

import se.ifmo.server.collection.CollectionManager;
import se.ifmo.server.db.UserService;
import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.model.Vehicle;

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
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        long uid = UserService.getInstance().getUserID(req.credentials().username());
        List<Vehicle> carFleet = VehicleDirector.constructAndGetRandomVehicles(Integer.parseInt(req.args().getFirst()), uid);
        CollectionManager.getInstance().addAll(carFleet);
        return new Callback("Successfully added new " + req.args().getFirst() + " elements to the collection");
    }
}
