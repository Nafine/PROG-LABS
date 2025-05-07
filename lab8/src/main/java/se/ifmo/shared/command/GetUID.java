package se.ifmo.shared.command;

import se.ifmo.server.db.UserService;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;

public class GetUID extends Command {
    public GetUID() {
        super("getUID", "Asks to give server user's ID");
    }

    /**
     * Currently outputs type of collection and its size.
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        return new Callback(String.valueOf(UserService.getInstance().getUserID(req.credentials().username())));
    }
}
