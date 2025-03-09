package se.ifmo.client.command;

import se.ifmo.client.builders.VehicleDirector;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.List;

public class AddRandom extends Command {
    public AddRandom() {
        super("add_random", new String[]{"amount"}, "Adds n random elements to the collection");
    }

    @Override
    public Callback execute(Request req) throws InvalidDataException {
        List<Vehicle> carFleet = VehicleDirector.constructAndGetRandomVehicles(Integer.parseInt(req.args().get(0)));
        CollectionManager.getInstance().getCollection().addAll(carFleet);
        return new Callback("Successfully added new " + req.args().get(0) + " elements to the collection");
    }
}
