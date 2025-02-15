package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class Exit extends Command {
    public Exit() {
        super("exit", "Exits from program");
    }

    @Override
    public Callback execute(Request req) {
        return new Callback("exit");
    }
}
