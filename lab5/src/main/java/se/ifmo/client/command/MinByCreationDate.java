package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;

import java.util.Comparator;

public class MinByCreationDate extends Command {
    public MinByCreationDate() {
        super("min_by_creation_date", "Output any object from the collection whose creationDate field value is minimal");
    }

    @Override
    public Callback execute(Request req) {
        var minVehicle = CollectionManager.getInstance().getCollection().stream().min(Comparator.comparing(Vehicle::getCreationDate));

        return minVehicle.map(vehicle -> new Callback(vehicle.toString())).orElseGet(() -> new Callback("Collection is empty"));
    }
}
