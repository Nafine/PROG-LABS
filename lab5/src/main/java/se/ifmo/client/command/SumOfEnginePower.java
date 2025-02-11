package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class SumOfEnginePower extends Command {
    public SumOfEnginePower() {
        super("sum_of_eninge_power", "Sum of eninge power");
    }

    @Override
    public Callback execute(Request req) {
        return Callback.empty();
    }
}
