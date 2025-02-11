package se.ifmo.client.command;

import se.ifmo.client.command.util.VehicleReader;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.CollectionElement;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.Comparator;

public class RemoveGreater extends Command {
    public RemoveGreater() {
        super("remove_greater", "Removes all elements that are greater than specified element");
    }

    @Override
    public Callback execute(Request req) {
        try {
            Vehicle vehicle = VehicleReader.readElement(req.console());

            CollectionManager.getInstance().getCollection().removeIf(temp -> temp.compareTo(vehicle) < 0);
            return new Callback("Successfully deleted all matching elements");
        } catch (InterruptedException e) {
            return new Callback("Command interrupted");
        } catch (InvalidDataException e) {
            return new Callback("You've input an invalid data");
        }
    }
}
