package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.enums.FuelType;
import se.ifmo.shared.model.Vehicle;

import java.util.LinkedHashSet;

/**
 * Filters and shows all elements with specified {@link FuelType}.
 */
public class FilterLessThanFuelType extends Command {
    /**
     * Constructs a new {@link FilterLessThanFuelType} command.
     */
    public FilterLessThanFuelType() {
        super("filter_less_than_fuel_type", new String[]{"fuelType"}, "Output items whose fuelType field value is less than the specified value");
    }

    /**
     * Filters all elements comparing them by fuel type.
     * <p>
     * Asks user to enter exactly one {@link FuelType}.
     * </p>
     *
     * @param request {@link Request}
     * @return {@link Callback}
     */
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
