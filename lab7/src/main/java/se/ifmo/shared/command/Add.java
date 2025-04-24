package se.ifmo.shared.command;

import se.ifmo.server.collection.CollectionManager;
import se.ifmo.server.db.UserService;
import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.exceptions.InvalidDataException;

/**
 * Adds new element to the collection.
 */
public class Add extends Command {
    /**
     * Constructs a new {@link Add} command.
     */
    public Add() {
        super("add", new String[]{"name", "coordinate_x", "coordinate_y", "engine_power", "capacity", "distance_traveled", "fuel_type"}, "Adds new element to the collection.\n" +
                "Fuel type choose from:\n" +
                "    GASOLINE,\n" +
                "    ALCOHOL,\n" +
                "    MANPOWER,\n" +
                "    NUCLEAR,\n" +
                "    PLASMA");
    }

    /**
     * Executes the command to add an element.
     * <p>
     * Asks user to enter exactly one {@link se.ifmo.shared.model.Vehicle} using {@link VehicleDirector}.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        long uid = UserService.getInstance().getUserID(req.credentials().username());
        if (CollectionManager.getInstance().add(VehicleDirector.constructAndGetVehicle(req.args(), uid)))
            return new Callback("Successfully added new element to the collection");
        else
            return new Callback("Failed to add new element to the collection");
    }
}
