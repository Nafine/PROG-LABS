package se.ifmo.client.command;

import se.ifmo.client.builders.VehicleDirector;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.exceptions.InvalidDataException;

/**
 * Adds new element to the collection.
 */
public class Add extends Command {

    /**
     * Constructs a new {@link Add} command.
     */
    public Add() {
        super("add", Command.EMPTY_ARGS, "Adds new element to the collection");
    }

    /**
     * Executes the command to add an element.
     * <p>
     * Asks user to enter exactly one {@link se.ifmo.system.collection.model.Vehicle} using {@link VehicleDirector}.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        CollectionManager.getInstance().getCollection().add(VehicleDirector.constructAndGetVehicle(req.args()));
        return new Callback("Successfully added new element to the collection");
    }
}
