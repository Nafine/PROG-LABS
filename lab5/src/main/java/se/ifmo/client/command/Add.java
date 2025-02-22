package se.ifmo.client.command;

import se.ifmo.client.command.util.VehicleReader;
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
     * Asks user to enter exactly one {@link se.ifmo.system.collection.model.Vehicle} using {@link VehicleReader}.
     * </p>
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        try {
            CollectionManager.getInstance().getCollection().add(VehicleReader.readElement(req.args()));
            return new Callback("Successfully added new element to the collection");
        } catch (IndexOutOfBoundsException e) {
            return new Callback("Wrong arguments (must be at least" + this.getArgs().length + ")");
        } catch (InvalidDataException e) {
            return new Callback("You've input an invalid data: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return new Callback("Wrong arguments: " + e.getMessage());
        }
    }
}
