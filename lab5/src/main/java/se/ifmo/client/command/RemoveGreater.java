package se.ifmo.client.command;

import se.ifmo.client.command.util.VehicleReader;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

public class RemoveGreater extends Command {
    public RemoveGreater() {
        super("remove_greater", new String[]{"name", "coordinate_x", "coordinate_y", "engine_power", "capacity", "distance_traveled", "fuel_type"}, "Removes all elements that are greater than specified element");
    }

    @Override
    public Callback execute(Request req) {
        try {
            Vehicle vehicle = VehicleReader.readElement(req.args());

            CollectionManager.getInstance().getCollection().removeIf(temp -> temp.compareTo(vehicle) < 0);
            return new Callback("Successfully deleted all matching elements");
        } catch (IndexOutOfBoundsException e) {
            return new Callback("Wrong arguments (must be at least" + this.getArgs().length + ")");
        } catch (InvalidDataException e) {
            return new Callback("You've input an invalid data: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return new Callback("Wrong arguments: " + e.getMessage());
        }
    }
}
