package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;

public class Exit extends Command {
    /**
     * Constructs a new {@link Exit} command.
     */
    public Exit() {
        super("exit", "Exits from program");
    }

    /**
     * Returns {@link Callback} which contains string "exit".
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        return new Callback("You can't shutdown server while being client");
    }
}
