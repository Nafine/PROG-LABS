package se.ifmo.client.command;

import se.ifmo.client.command.util.VehicleReader;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.exceptions.InvalidDataException;

public class Add extends Command {

    public Add() {
        super("add", new String[]{"name", "coordinate_x", "coordinate_y", "engine_power", "capacity", "distance_traveled", "fuel_type"}, "Adds new element to the collection");
    }

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
