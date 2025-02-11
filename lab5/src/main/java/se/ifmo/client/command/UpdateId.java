package se.ifmo.client.command;

import se.ifmo.client.command.util.VehicleReader;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.LinkedHashSet;

public class UpdateId extends Command {

    public UpdateId() {
        super("update_id", new String[]{"id"}, "update the value of the collection item whose id is equal to the given one");
    }

    @Override
    public Callback execute(Request req) {
        try {
            int id = Integer.parseInt(req.args().get(0));

            Vehicle vehicle = VehicleReader.readElement(req.console());
            vehicle.setId(id);

            LinkedHashSet<Vehicle> collection = CollectionManager.getInstance().getCollection();
            collection.remove(collection.stream().filter(temp -> temp.getId() == id).findFirst().get());
            collection.add(vehicle);

            return new Callback("Successfully updated an element by id " + req.args().get(0));
        } catch (InterruptedException e) {
            return new Callback("Command got interrupted.");
        } catch (InvalidDataException e) {
            return new Callback("You've input an invalid data.");
        } catch (IllegalArgumentException e) {
            return new Callback("Wrong command arguments.");
        }
    }
}
