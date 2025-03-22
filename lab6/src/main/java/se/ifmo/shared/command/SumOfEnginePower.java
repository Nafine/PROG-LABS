package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.model.Vehicle;

/**
 * Returns sum of engine power of all elements.
 */
public class SumOfEnginePower extends Command {
    /**
     * Constructs a new {@link RemoveById} command.
     */
    public SumOfEnginePower() {
        super("sum_of_engine_power", "Sum of enginePower of all elements");
    }

    /**
     * Summarizes all engine power from all elements.
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        int sum = 0;
        for (Vehicle vehicle : CollectionManager.getInstance().getCollection())
            sum += vehicle.getEnginePower();
        return new Callback(Integer.toString(sum));
    }
}
