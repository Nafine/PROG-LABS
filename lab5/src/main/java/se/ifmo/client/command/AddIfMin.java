package se.ifmo.client.command;

import se.ifmo.client.command.util.VehicleReader;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.Comparator;

public class AddIfMin extends Command {
    public AddIfMin() {
        super("add_if_min", "Add a new element to a collection if its value is less than the smallest element of this collection");
    }

    @Override
    public Callback execute(Request req) {
        try {
            Vehicle vehicle = VehicleReader.readElement(req.console());

            var minVehicle = CollectionManager.getInstance().getCollection().stream().min(Comparator.naturalOrder());
            if (minVehicle.isPresent() && vehicle.compareTo(minVehicle.get()) < 0) {
                CollectionManager.getInstance().getCollection().add(minVehicle.get());
                return new Callback("Successfully added new element to the collection");
            }
            return new Callback("Failed to add new element to the collection, it's not the smallest element");
        } catch (InterruptedException e) {
            return new Callback("Command interrupted");
        } catch (InvalidDataException e) {
            return new Callback("You've input an invalid data");
        }
    }
}
