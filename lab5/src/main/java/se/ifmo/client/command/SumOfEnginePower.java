package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;

public class SumOfEnginePower extends Command {
    public SumOfEnginePower() {
        super("sum_of_engine_power", "Sum of enginePower of all elements");
    }

    @Override
    public Callback execute(Request req) {
        int sum = 0;
        for (Vehicle vehicle : CollectionManager.getInstance().getCollection())
            sum += vehicle.getEnginePower();
        return new Callback(Integer.toString(sum));
    }
}
