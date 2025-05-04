package se.ifmo.shared.command;

import se.ifmo.server.db.UserService;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.exceptions.InvalidDataException;

/**
 * Client-side command used to log in user.
 */
public class Login extends Command {
    /**
     * Constructs a new {@link Add} command.
     */
    public Login() {
        super("login", "Command which used to check username and password.");
    }

    /**
     * Executes the command to user login.
     * <p>
     * Asks server about existing pair (login, password), and returns corresponding callback.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        return UserService.getInstance().comparePassword(req.credentials()) ? Callback.successfulLogin() : Callback.wrongCredentials();
    }
}
