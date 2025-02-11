package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.enums.FuelType;
import se.ifmo.system.collection.model.Vehicle;

import java.util.LinkedHashSet;

public class FilterLessThanFuelType extends Command {
    public FilterLessThanFuelType() {
        super("filter_less_than_fuel_type", new String[]{"fuelType"}, "Output items whose fuelType field value is less than the specified value");
    }

    @Override
    public Callback execute(Request request) {
        LinkedHashSet<Vehicle> collection = CollectionManager.getInstance().getCollection();
        if (collection.isEmpty()) return new Callback("Collection is empty");

        return new Callback("Found %d items:".formatted(collection.size()),
                collection.stream().filter(
                vehicle -> vehicle.getFuelType().ordinal() < FuelType.valueOf(request.args().get(0)).ordinal())
                .toList());
    }
}
