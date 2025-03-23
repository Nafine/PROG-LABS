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
        System.exit(0);
        return new Callback("How did you manage to see this callback?");
    }
}
