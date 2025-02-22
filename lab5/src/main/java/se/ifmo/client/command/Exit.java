package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class Exit extends Command {
    /**
     * Constructs a new {@link Exit} command.
     */
    public Exit() {
        super("exit", "Exits from program");
    }

    /**
     * Returns {@link Callback} which contains string "exit".
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        return new Callback("exit");
    }
}
